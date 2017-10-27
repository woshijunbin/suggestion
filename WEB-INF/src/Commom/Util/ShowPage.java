package Commom.Util;
/**
 * 分页类
 */
public class ShowPage {
	private int intPageSize = Integer.valueOf(ReadConfigInfo.getProperty("pageSize")); // 一页显示的记录数
	private int intRowCount; // 记录总数
	private int intPage = 1; // 待显示页数
	public ShowPage() {
		super();
	}
	public int getIntPageSize() {
		return intPageSize;
	}
	public void setIntPageSize(int intPageSize) {
		this.intPageSize = intPageSize;
	}
	public int getIntRowCount() {
		return intRowCount;
	}
	/**
	 * 设置搜索后要显示的行数
	 */
	public void setIntRowCount(int intRowCount) {
		this.intRowCount = intRowCount;
	}
	public int getIntPageCount() {
		int count = (getIntRowCount() + getIntPageSize() - 1)
				/ getIntPageSize();
		if (count <= 0) {
			count = 1;
		}
		return count;
	}
	public int getIntPage() {
		if (intPage >= getIntPageCount() && getIntPageCount() > 0) {
			this.intPage = getIntPageCount();
		} else if (intPage <= 1) {
			this.intPage = 1;
		}
		return intPage;
	}
	/**
	 * 设置显示第几页
	 */
	public void setIntPage(int intPage) {
		this.intPage = intPage;
	}
	public int getStartRow() {
		return getIntPage() > 1 ?(getIntPage() - 1) * getIntPageSize() : 0;
	}
	public int getXuhao() {
		return (intPage - 1) * intPageSize + 1;
	}
}
