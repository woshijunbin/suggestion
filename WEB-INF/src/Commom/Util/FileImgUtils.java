package Commom.Util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 文件(图片，一般文件)工具类<br>
 * 
 * @author User
 */
public final class FileImgUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileImgUtils.class);

	/**
	 * 文件复制<br>
	 * 不允许同一目录下同名文件复制
	 * 
	 * @param srcPath
	 *            源文件绝对路径(E:\jianguoyun\v0.1Project\ckeditor-v0.1/name.jpg)
	 * @param destPath
	 *            目标目录
	 * @param fileName
	 *            自定义新文件名称(xx.jpg)要加扩展名
	 * @param overlay
	 *            是否覆盖已有文件。true覆盖
	 * @param delete
	 *            是否删除源图片。true删除
	 */
	public static void copy2Dest(String srcPath, String destPath, String fileName, boolean overlay, boolean delete)
			throws Exception {
		File srcFile = new File(srcPath); // 源文件
		File destFile = null; // 目标文件

		if (fileName != null && fileName.length() > 0) {
			destFile = new File(destPath, fileName);
		} else {
			destFile = new File(destPath, srcFile.getName());
			if (!overlay && destFile.exists()) {
				System.err.println("文件已经存在，不允许覆盖");
				return;
			}
		}

		try {
			FileUtils.copyFile(srcFile, destFile);
			if (delete) {
				srcFile.delete();//删除原文件
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ########################################################################################################################################
	/*
	 * 文件上传操作http://www.blogjava.net/conans/articles/214085.html
	 * struts1使用FormFile来处理关于文件上传的模块，我们处理文件实际文件已经是上传完成了。如果一旦文件大小大于服务器承受范围，
	 * 我们是无法控制的。
	 * 
	 */
	// #######################################################################################################################################
	private long maxSize = 1024 * 1024 * 70;// 70M。可接收request实体内容大小byte
	private long fileSizeMax = 1024 * 1024 * 70;// 70M。可接收每个上传文件最大值byte
	private int sizeThreshold = 1024;// 使用临时目录阈值，超出该值的数据缓存到临时文件。
	private File repository = new File(System.getProperty("java.io.tmpdir"));// 临时目录路径
	private String defaultEncode = "utf-8";// utf-8

	/**
	 * 获取ServletFileUpload
	 * @param encode
	 *            上传文件保存编码
	 */
	public ServletFileUpload getFileUpload(String encode) {

		FileItemFactory factory = new DiskFileItemFactory(sizeThreshold, repository);
		ServletFileUpload upload = new ServletFileUpload(factory);

		if (encode != null && encode.length() > 0) {
			upload.setHeaderEncoding(encode);
		} else {
			upload.setHeaderEncoding(defaultEncode);
		}

		upload.setSizeMax(maxSize);
		upload.setFileSizeMax(fileSizeMax);
		return upload;
	}

	/**
	 * 文件上传(仅上传功能，同名文件将覆盖)
	 * 
	 * @param targetDir
	 *            上传目录路径(项目本地路径)
	 * @param relative
	 *            上传路径是否相对路径(相对路径如：C:/img直接给/img)
	 * @param encode
	 *            文件保存编码
	 * @param rename
	 *            重命名
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public List<FileInfo> uploadFiles(String targetDir, boolean relative, String encode, boolean rename,
			HttpServletRequest request) {
		List<FileInfo> fileInfos = new ArrayList<FileInfo>(0);
		/*
		 * tomcat6(servlet2.5不支持request.getServletContext())
		 */
		File dir = relative ? (new File(request.getSession().getServletContext().getRealPath(""), targetDir))
				: (new File(targetDir));
		if (!dir.exists())
			dir.mkdirs();

		ServletFileUpload upload = getFileUpload(encode);
		try {

			// 解析请求
			/*
			 * 请求流处理方式、不产生临时文件。更高效： FileItemIterator fileItems =
			 * upload.getItemIterator(request);
			 * 
			 * fileItems处理方式： List<FileItem> items =
			 * upload.parseRequest(request);
			 */
			FileItemIterator fileItems = upload.getItemIterator(request);
			while (fileItems.hasNext()) {
				FileItemStream itemStream = fileItems.next();
				if (!itemStream.isFormField()) {
					FileInfo fileInfo = new FileInfo();
					String fileNamePath = itemStream.getName();// 可能包含路径的文件名
					if (fileNamePath == null || fileNamePath.length() < 1) {
						LOGGER.warn("name=" + itemStream.getFieldName() + "表单项未提供图片上传");
						continue;// 忽略未上传文件的表单项
					}
					String fileName = "";// 不带后缀的文件名称
					String ext = "";// 文件扩展(.txt)

					int pointIndex = fileNamePath.indexOf(".");
					int slashIndex = fileNamePath.indexOf("\\");
					if (pointIndex > -1) {
						ext = fileNamePath.substring(pointIndex);
						fileName = fileNamePath.substring(slashIndex > -1 ? slashIndex + 1 : 0, pointIndex);
					} else {
						fileName = slashIndex > -1 ? fileNamePath.substring(slashIndex + 1) : fileNamePath;
					}

					// 重命名 逻辑
					String newFileName = rename ? (UUID.randomUUID().toString() + ext) : (fileName + ext);
					File file = new File(dir, newFileName);

					InputStream in = itemStream.openStream();
					FileUtils.copyInputStreamToFile(in, file);

					fileInfo.setFilename(fileName);
					fileInfo.setAbsolutePath(file.getAbsolutePath());
					fileInfo.setNewFilename(newFileName);
					fileInfo.setSize(String.valueOf(file.length()));
					fileInfo.setType(ext);
					fileInfos.add(fileInfo);
				}
			}

		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileInfos;
	}

	/**
	 * 批量 固定宽高图片压缩与上传;非图片文件忽略上传
	 * 
	 * @param targetDir
	 *            上传目录路径,末尾要带'/'
	 * @param encode
	 *            文件保存编码
	 * @param rename
	 *            重命名(重命名使用uuid)
	 * @param request
	 * @param width
	 *            图片最终宽度
	 * @param height
	 *            图片最终高度
	 * @param quality
	 *            压缩质量(不大于1)
	 * @return
	 * @throws Exception
	 */
	public List<FileInfo> zippedImages(String targetDir, String encode, boolean rename, HttpServletRequest request,
			int width, int height, float quality) {
		List<FileInfo> fileInfos = new ArrayList<FileInfo>(0);

		/* tomcat6(servlet2.5不支持request.getServletContext()) */
		// dir = new
		// File(request.getServletContext().getRealPath(""),targetDir);
		File dir = new File(targetDir);
		if (!dir.exists())
			dir.mkdirs();
		if (!dir.isDirectory()) {
			LOGGER.warn("未正确提供文件目录");
			return fileInfos;
		}

		try {
			// 解析请求
			/*
			 * 请求流处理方式、不产生临时文件。更高效： FileItemIterator fileItems =
			 * upload.getItemIterator(request);
			 * 
			 * fileItems处理方式： List<FileItem> items =
			 * upload.parseRequest(request);
			 */
			ServletFileUpload upload = getFileUpload(encode);
			FileItemIterator fileItems = upload.getItemIterator(request);
			while (fileItems.hasNext()) {
				FileItemStream itemStream = fileItems.next();
				if (!itemStream.isFormField()) {
					FileInfo fileInfo = new FileInfo();
					String fileNamePath = itemStream.getName();// 可能包含路径的文件名
					if (fileNamePath == null || fileNamePath.length() < 1) {
						LOGGER.warn("name=" + itemStream.getFieldName() + "表单项未提供图片上传");
						// 忽略未上传文件的表单项
						continue;
					}
					String fileName = "";// 不带后缀的文件名称
					String ext = validateMime(itemStream, mimeArray);// 文件扩展名称(.jpg)
					if (ext.length() < 1) {
						LOGGER.warn("name=" + itemStream.getFieldName() + "表单项未提供正确的图片格式文件");
						// 忽略非图片格式文件上传
						continue;
					}

					int pointIndex = fileNamePath.indexOf(".");
					int slashIndex = fileNamePath.lastIndexOf("\\");
					if (pointIndex > -1) {
						fileName = fileNamePath.substring(slashIndex > -1 ? slashIndex + 1 : 0, pointIndex);
					} else {
						fileName = slashIndex > -1 ? fileNamePath.substring(slashIndex + 1) : fileNamePath;
					}

					// 重命名逻辑
					String newFileName = rename ? (UUID.randomUUID().toString() + ext) : (fileName + ext);

					InputStream in = itemStream.openStream();

					FileImgUtils.saveminPhoto(targetDir + newFileName, in, width, height, quality);

					File file = new File(targetDir + newFileName);
					fileInfo.setFilename(fileName);
					fileInfo.setNewFilename(newFileName);
					fileInfo.setSize(String.valueOf(file.length()));
					fileInfo.setAbsolutePath(file.getAbsolutePath());
					fileInfo.setType(ext);
					fileInfos.add(fileInfo);
				}
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileInfos;
	}

	/**
	 * 批量 指定宽度，宽高压缩比图片压缩与上传;非图片文件忽略上传
	 * 
	 * @param targetDir
	 *            目标文件绝对路径(包含文件名)
	 * @param encode
	 *            文件保存编码
	 * @param rename
	 *            是否重命名(重命名为uuid)
	 * @param request
	 * @param baseWidth
	 *            基准宽度(目标图片不大于该值)
	 * @param scale
	 *            基准宽高比例
	 * @param quality
	 *            压缩质量(不大于1)
	 * @return
	 */
	public List<FileInfo> zippedImages(String targetDir, String encode, boolean rename, HttpServletRequest request,
			int baseWidth, double scale, float quality) {
		List<FileInfo> fileInfos = new ArrayList<FileInfo>(0);

		/* tomcat6(servlet2.5不支持request.getServletContext()) */
		// dir = new
		// File(request.getServletContext().getRealPath(""),targetDir);
		File dir = new File(targetDir);
		if (!dir.exists())
			dir.mkdirs();
		if (!dir.isDirectory()) {
			LOGGER.warn("未提供正确的文件目录");
			return fileInfos;
		}

		try {
			// 解析请求
			/*
			 * 请求流处理方式、不产生临时文件。更高效： FileItemIterator fileItems =
			 * upload.getItemIterator(request);
			 * 
			 * fileItems处理方式： List<FileItem> items =
			 * upload.parseRequest(request);
			 */
			ServletFileUpload upload = getFileUpload(encode);
			FileItemIterator fileItems = upload.getItemIterator(request);
			while (fileItems.hasNext()) {
				FileItemStream itemStream = fileItems.next();
				if (!itemStream.isFormField()) {
					FileInfo fileInfo = new FileInfo();
					String fileNamePath = itemStream.getName();// 可能包含路径的文件名
					if (fileNamePath == null || fileNamePath.length() < 1) {
						LOGGER.warn("name=" + itemStream.getFieldName() + "表单项未提供上传文件");
						// 忽略未上传文件的表单项
						continue;
					}
					String fileName = "";// 不带后缀的文件名称
					String ext = validateMime(itemStream, mimeArray);// 文件扩展名称(.jpg)
					if (ext.length() < 1) {
						LOGGER.warn("name=" + itemStream.getFieldName() + "表单项未提供正确的图片格式文件");
						// 忽略非图片格式文件上传
						continue;
					}

					int pointIndex = fileNamePath.indexOf(".");
					int slashIndex = fileNamePath.lastIndexOf("\\");
					if (pointIndex > -1) {
						fileName = fileNamePath.substring(slashIndex > -1 ? slashIndex + 1 : 0, pointIndex);
					} else {
						fileName = slashIndex > -1 ? fileNamePath.substring(slashIndex + 1) : fileNamePath;
					}

					// 重命名逻辑
					String newFileName = rename ? (UUID.randomUUID().toString() + ext) : (fileName + ext);

					InputStream in = itemStream.openStream();
					FileImgUtils.saveminPhoto(targetDir + newFileName, in, baseWidth, scale, quality);

					File file = new File(targetDir + newFileName);
					fileInfo.setFilename(fileName);
					fileInfo.setNewFilename(newFileName);
					fileInfo.setSize(String.valueOf(file.length()));
					fileInfo.setAbsolutePath(file.getAbsolutePath());
					fileInfo.setType(ext);
					fileInfos.add(fileInfo);
				}
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileInfos;
	}

	/* 定义图片类型 */
	public String[][] mimeArray = { { "image/pjpeg", ".jpg" }, { "image/jpeg", ".jpg" }, { "image/png", ".png" },
			{ "image/png", ".png" }, { "image/gif", ".gif" }, { "image/bmp", ".bmp" } };

	/**
	 * 文件图片类型验证(通过服务器MIME类型)<br>
	 * 非图片类型返回""
	 * 
	 * @return String 图片扩展名称
	 */
	public String validateMime(FileItemStream itemStream, String[][] mimeArray) {
		String ext = "";// 文件扩展名(.jpg)

		// mime类型校验
		String contentType = itemStream.getContentType();
		if (mimeArray.length < 0)
			return "";
		if (contentType == null || contentType.length() < 1)
			return "";
		for (String[] mimeMap : mimeArray) {
			/** if (contentType.equals("image/pjpeg"){ext = ".jpg";} */
			if (contentType.equalsIgnoreCase(mimeMap[0])) {
				ext = mimeMap[1];
			}
		}
		return ext;
	}

	/**
	 * 直接指定宽高进行图片缩小或放大
	 * 
	 * @param tarFilePath
	 *            图片输出绝对路径(含文件名称)
	 * @param inputStream
	 *            源图输入流
	 * @param width
	 *            压缩后的宽度
	 * @param height
	 *            压缩后的高度
	 * @param quality
	 *            压缩质量
	 */
	public static void saveminPhoto(String tarFilePath, InputStream inputStream, int width, int height, float quality) {
		if (tarFilePath == null || tarFilePath.length() < 1) {
			LOGGER.warn("文件路径不允许空");
			return;
		}
		try {
			Image srcImage = inputStream == null ? ImageIO.read(new File(tarFilePath)) : ImageIO.read(inputStream);
			/** 宽,高设定 */
			BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			tag.getGraphics().drawImage(srcImage, 0, 0, width, height, null);

			FileOutputStream out = new FileOutputStream(tarFilePath);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(tag);
			jep.setQuality(quality, true);
			encoder.encode(tag, jep);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据基准宽度、基准宽高比例缩放图片，当源图宽高都小于基准宽高，使用源图格式复制图片
	 *
	 * @param tarFilePath
	 *            目标图片绝对路径(包含文件名)
	 * @param inputStream
	 *            源图输入流
	 * @param baseWidth
	 *            基准宽度(限制最大宽度)
	 * @param scale
	 *            基准宽高比(原图片宽高比)
	 */
	public static void saveminPhoto(String tarFilePath, InputStream inputStream, int baseWidth, double scale,
			float quality) {

		if (tarFilePath == null || tarFilePath.length() < 1) {
			LOGGER.warn("文件路径不允许空");
			return;
		}
		try {
			Image src = inputStream == null ? ImageIO.read(new File(tarFilePath)) : ImageIO.read(inputStream);

			int srcHeight = src.getHeight(null);
			int srcWidth = src.getWidth(null);
			double srcScale = new Double(srcWidth) / srcHeight;
			// 计算基准最大限制的高度
			int baseHeight = new Double(baseWidth / scale).intValue();
			// 生成图宽高
			int deskHeight = 0;// 缩略图高
			int deskWidth = 0;// 缩略图宽
			if (srcHeight < baseHeight && srcWidth < baseWidth) {
				deskHeight = srcHeight;
				deskWidth = srcWidth;
			} else {
				if (srcHeight > baseHeight && srcWidth < baseWidth) {
					deskHeight = baseHeight;
					deskWidth = new Double(baseHeight * srcScale).intValue();
				} else {
					// srcWidth > baseWidth && srcHeight < baseHeight
					// srcWidth > baseWidth && srcHeight > baseHeight
					deskWidth = baseWidth;
					deskHeight = new Double(baseWidth / srcScale).intValue();
				}
			}

			BufferedImage tag = new BufferedImage(deskWidth, deskHeight, BufferedImage.TYPE_3BYTE_BGR);
			tag.getGraphics().drawImage(src, 0, 0, deskWidth, deskHeight, null); // 绘制缩小后的图
			FileOutputStream deskImage = new FileOutputStream(tarFilePath); // 输出到文件流
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(deskImage);
			encoder.encode(tag); // 近JPEG编码
			deskImage.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public long getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(long maxSize) {
		this.maxSize = maxSize;
	}

	public long getFileSizeMax() {
		return fileSizeMax;
	}

	public void setFileSizeMax(long fileSizeMax) {
		this.fileSizeMax = fileSizeMax;
	}

	public int getSizeThreshold() {
		return sizeThreshold;
	}

	public void setSizeThreshold(int sizeThreshold) {
		this.sizeThreshold = sizeThreshold;
	}

	public File getRepository() {
		return repository;
	}

	public void setRepository(File repository) {
		this.repository = repository;
	}

	public String getDefaultEncode() {
		return defaultEncode;
	}

	public void setDefaultEncode(String defaultEncode) {
		this.defaultEncode = defaultEncode;
	}

	public String[][] getMimeArray() {
		return mimeArray;
	}

	public void setMimeArray(String[][] mimeArray) {
		this.mimeArray = mimeArray;
	}

	public static void main(String[] args) throws Exception {
		String src = "C:/Users/User/Desktop/01.jpg";
		String targetDir = "C:/Users/User/Desktop/a/1.jpg";
		FileImgUtils.saveminPhoto(targetDir, new FileInputStream(src), 2000, 1920.0 / 1080, 1f);

	}
}
