/**
 * 使用请先加载jquery
 * stepFlag 键盘上下键触发时，触发滚动条滚动。值为显示的数据条数-1
 * 
 * @param options对象：
 * selector,url，key,callback
 */
function suggestion(options){
    var step = 0,
        stepFlag =7;  

    // 单行高亮显示
    var addPreview  = function(that) {
        $('.preview').removeClass('preview');
        if(that.constructor == $) {
            that.addClass('preview');
        }
    }

    /**
     * 获取值。
     * 限制传入对象必须是jquery对象
     */
    var getVal = function(that) {
        return that.constructor == $  && that.data('val') || that.text();
        /*
                        下面return后换行再写结果都是 undefined 。 js的分号补全机制做鬼
        return 
            that.constructor == $  && that.data('val') || that.text();
        */
    }
    
    
    
    $(options.selector).each(function() {
        var $that = $(this),
            $searchSelect = $("<div class='searchSelect'></div>"),
            $searchIcon = $("<i class='searchIcon'></i>"),
            $searchDl = $("<dl class='searchDl'><p class='noneItem'>无匹配项</p></dl>");

        // input的keydown事件
        var keydown = function(e) {
            var that = e.target,
                thatClass = that.getAttribute('class'),
                keycode = e.keyCode,
                scrolltop = $searchDl.scrollTop(),
                $preview = $('dd.preview');
            
            if(keycode == 38) { // 上方向键
                if($preview.prev().length){
                    if(step == 0)  {
                        $searchDl.scrollTop(scrolltop-36)
                    }else {
                        step --
                        step = step>0?step:0
                    }
                    addPreview($preview.prev())
                    $that.val(getVal($preview.prev()))
                    return false;// 阻止input默认事件   
                }
            }else if(keycode == 40) { // 下方向键
                if($preview.next().length){
                    if(step == stepFlag) {
                        $searchDl.scrollTop(scrolltop+36)
                    }else {
                        step ++
                        step = step > stepFlag?stepFlag:step
                    }
                    addPreview($preview.next())
                    $that.val(getVal($preview.next()))
                }
            }else if(keycode == 13){ // 回车键
                $searchDl.removeClass('searchShow');
                $that.val(getVal($preview))
            }
        }


        // input 的值改变事件
        var change = function () {
        	console.log('change事件')
            var param = {};
            step = 0;
            param[options.key] = $that.val();
            $.ajax({
                type : "post",
                dataType : "html",
                url : options.url,
                data : param,
                success : function(data) {
                    $searchDl.empty();
                    options.callback.apply(data,[$searchDl])
                    $searchDl.find('dd:first').addClass('preview')
                },
                error : function() {
                    console.log('error')
                },
                complete : function() {
                    console.log('complete')
                }
            });
        }
        
        $that.wrap($searchSelect).after($searchIcon).after($searchDl);
        $that.on('keydown',keydown); 
        $that.on('input propertychange focus',change);       
        
        
    }) 
    
    
    $(document).on('click',function(e) {
        var 
            that = e.target,
            thatClass = that.getAttribute('class');
        
        $('.searchDl').removeClass('searchShow');
        // 点击input 展开-关闭选项面板
        if(thatClass == 'searchInput') {
        	$(that).next('.searchDl').addClass('searchShow');
        }


        // 点击 选项
        if(thatClass == 'searchDd') {
            $(that).parent().siblings('.searchInput').val(getVal($(that)));
        }
        
    })


}