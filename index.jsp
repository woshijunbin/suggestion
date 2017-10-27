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

	<!-- 无缝嵌入layui样式 -->
	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label">layui样式</label>
			<div class="layui-input-inline">
				<input class="searchInput" name="type3" id="searchInput" autocomplete="off" type="text" placeholder="搜索" value="" > 
			</div>
	    </div>
	</div>
    
    
    <!--  suggestion标准写法 -->
    <input class="searchInput" name="type3" id="searchInput" autocomplete="off" type="text" placeholder="搜索" value="" > 
    <input class="1" name="type3" id="searchInput" autocomplete="off" type="text" placeholder="搜索" value="" > 



<script type="text/javascript">
suggestion({
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