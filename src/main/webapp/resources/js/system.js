window.onload = function(){
	$('#loading-mask').fadeOut();
}
var onlyOpenTitle="首页";
$(function(){
	openPwd();
	init();
	tabClose();
	tabCloseEven();
})

function selectPage(pageNumber, pageSize){
    $("#page").val(pageNumber);
    $("#pageSize").val(pageSize);
    submitForm();
}

function show(title,msg){
	if(title==''){
		title = "系统提示";
	}
	if(msg==''){
		msg = "系统错误，请稍候再试！";
	}
	 $.messager.show({
		 title:title,
		 msg:msg,
		timeout:3000,
		showType:'slide'
		 });
}

function alertMsg(msg,type){
		 $.messager.alert('系统提示',msg,type);
}

function submitForm(){
    $("#inputForm").submit();
}

//初始化左侧
function init() {
	$('#editpass').click(function() {
        $('#w').window('open');
     });

     $('#btnEp').click(function() {
           serverLogin();
     })

	$('#btnCancel').click(function(){closePwd();})

    $('#loginOut').click(function() {
        $.messager.confirm('系统提示', '您确定要退出本次登录吗?', function(r) {
            if (r) {

            	var curWwwPath = window.document.location.href;
          		var pathName =  window.document.location.pathname;
          		var pos = curWwwPath.indexOf(pathName);
          		var localhostPaht = curWwwPath.substring(0,pos);
          		var projectName = pathName.substring(0,pathName.substr(1).indexOf('/')+1);
          		var root = localhostPaht + projectName;
            	location.href = root+'/logout.html';
            }
        });
    });
			
	var _tabPanel=$("#tabs");
    $(".easyui-accordion").find("a[link]").each(function(){
        $(this).click(function(){
            var url=$(this).attr("link");
            var title=$(this).html();
	        if(url!=="#"&&url!==""){
	            if(!_tabPanel.tabs('exists',title)){
			        addTab(title,url,'');
		        }else{
			        _tabPanel.tabs('select',title);
			      //刷新当前tab
			        var currTab =  _tabPanel.tabs('getSelected'); //获得当前tab
			        var url = $(currTab.panel('options').content).attr('src');
			        self.parent.$('#tabs').tabs('update', {
			          tab : currTab,
			          options : {
			           content : createFrame(url)
			          }
			         });
		        }
	        }
        });
    });
}

//设置修改密码窗口
function openPwd() {
    $('#w').window({
        title: '修改密码',
        width: 300,
        modal: true,
        shadow: true,
        closed: true,
        height: 160,
        resizable:false
    });
}

//关闭修改密码窗口
function closePwd() {
    $('#w').window('close');
}



function createFrame(url){
	var s = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
	return s;
}

function addTab(subtitle,url,icon){
	if(!$('#tabs').tabs('exists',subtitle)){
		$('#tabs').tabs('add',{
			title:subtitle,
			content:createFrame(url),
			closable:true,
			icon:icon
		});
	}else{
		$('#tabs').tabs('select',subtitle);
		$('#mm-tabupdate').click();
	}
	tabClose();
}

function tabClose()
{
	/*双击关闭TAB选项卡*/
	$(".tabs-inner").dblclick(function(){
		var subtitle = $(this).children(".tabs-closable").text();
		$('#tabs').tabs('close',subtitle);
	})
		/*为选项卡绑定右键*/
		$(".tabs-inner").bind('contextmenu',function(e){
			$('#mm').menu('show', {
				left: e.pageX,
				top: e.pageY
		});

		var subtitle =$(this).children(".tabs-closable").text();
		$('#mm').data("currtab",subtitle);
		$('#tabs').tabs('select',subtitle);
		return false;
	});
}


//绑定右键菜单事件
function tabCloseEven() {
    $('#mm').menu({
        onClick: function (item) {
            closeTab(item.id);
        }
    });

    return false;
}

function closeTab(action){
    var alltabs = $('#tabs').tabs('tabs');
    var currentTab =$('#tabs').tabs('getSelected');
	var allTabtitle = [];
	$.each(alltabs,function(i,n){
		allTabtitle.push($(n).panel('options').title);
	});
    switch (action) {
        case "refresh":
            var iframe = $(currentTab.panel('options').content);
            var src = iframe.attr('src');
            $('#tabs').tabs('update', {
                tab: currentTab,
                options: {
                    content: createFrame(src)
                }
            })
            break;
        case "close":
            var currtab_title = currentTab.panel('options').title;
            if (tabIndex == 1||n==onlyOpenTitle) { 
                return false;
            }
            $('#tabs').tabs('close', currtab_title);
            break;
        case "closeall":
            $.each(allTabtitle, function (i, n) {
                if (n != onlyOpenTitle){
                    $('#tabs').tabs('close', n);
				}
            });
            break;
        case "closeother":
            var currtab_title = currentTab.panel('options').title;
            $.each(allTabtitle, function (i, n) {
                if (n != currtab_title && n != onlyOpenTitle)
				{
                    $('#tabs').tabs('close', n);
				}
            });
            break;
        case "closeright":
            var tabIndex = $('#tabs').tabs('getTabIndex', currentTab);

            if (tabIndex == alltabs.length - 1){
                alert('后边没有了');
                return false;
            }
            $.each(allTabtitle, function (i, n) {
                if (i > tabIndex) {
                    if (n != onlyOpenTitle){
                        $('#tabs').tabs('close', n);
					}
                }
            });

            break;
        case "closeleft":
            var tabIndex = $('#tabs').tabs('getTabIndex', currentTab);
            if (tabIndex == 1) {
                alert('只有一个标签了');
                return false;
            }
            $.each(allTabtitle, function (i, n) {
                if (i < tabIndex) {
                    if (n != onlyOpenTitle){
                        $('#tabs').tabs('close', n);
					}
                }
            });

            break;
        case "exit":
            $('#closeMenu').menu('hide');
            break;
    }
}


//弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
function msgShow(title, msgString, msgType) {
	$.messager.alert(title, msgString, msgType);
}
