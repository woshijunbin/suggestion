<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <link rel="stylesheet" type="text/css" href="reset.css">
    <link rel="stylesheet" type="text/css" href="suggestion.css">
    <link rel="stylesheet" type="text/css" href="static/plugin/layui/css/layui.css">
    <script type="text/javascript" src="http://apps.bdimg.com/libs/jquery/2.1.4/jquery.js"></script>
    <script type="text/javascript" src="suggestion.js"></script>
</head>
<body>
    <!-- 
    suggestion标准写法 
    	1.class指定为searchInput
    	2.调用suggestion(options)方法。
    	options属性介绍：
    		selector：希望渲染的元素选择器,
    		url：后台查询url,
    		key：后台接收的参数名,
    		callback:后台成功返回数据的回调函数:
    			suggestion内部会返回一个jquery对象传入$searchDl(名字随便取),这个对象代表了查出内容的父标签dl
    			this为后台返回的数据,
				参照下面写法。
				
     --> 
    <input class="searchInput" name="type3" autocomplete="off" type="text" placeholder="搜索" value="" > 

	<!-- 下面是嵌入layui样式的写法 -->
	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label">layui样式</label>
			<div class="layui-input-inline">
				<input class="searchInput" name="type3"  autocomplete="off" type="text" placeholder="搜索" value="" > 
			</div>
	    </div>
	</div>
    
    

<script type="text/javascript">
suggestion({
	selector:'.searchInput',
    url:'pinyinList.do',
    key:'firstChar',
    callback:function ($searchDl) {
        var data = this
        if (JSON.parse(data).length == 0) {
            $searchDl.append('<p class="noneItem">无匹配项</p>');
        } else {
            $.each(JSON.parse(data), function(index, val) {
                var dd = "<dd class='searchDd' data-val=" + val + ">" + val + "<p class = 'searchp'>你好</p></dd>";
                $searchDl.append(dd);
            });
        }
    },
});
</script>


</body>
</html>