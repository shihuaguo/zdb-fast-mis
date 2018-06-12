//jqGrid的配置信息
if($.jgrid){
	$.jgrid.defaults.width = 1000;
	$.jgrid.defaults.responsive = true;
	$.jgrid.defaults.styleUI = 'Bootstrap';
}

//工具集合Tools
window.T = {};

// 获取请求参数
// 使用示例
// location.href = http://localhost/index.html?id=123
// T.p('id') --> 123;
var url = function(name) {
	var reg = new RegExp("(^|&|&amp;)"+ name +"=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r!=null)return  unescape(r[2]); return null;
};
T.p = url;

//请求前缀
//var baseURL = "http://www.juziku.com/";
//var baseURL = "/renren-fast/";
var baseURL = "/financial/";

//登录token
var token = localStorage.getItem("token");
if(token == 'null'){
    parent.location.href = baseURL + 'login.html';
}

//jquery全局配置
$.ajaxSetup({
	dataType: "json",
	cache: false,
    headers: {
        "token": token
    },
    xhrFields: {
	    withCredentials: true
    },
    complete: function(xhr) {
        //token过期，则跳转到登录页面
        if(xhr.responseJSON && xhr.responseJSON.code == 401){
            parent.location.href = baseURL + 'login.html';
        }
    }
});

if($.jgrid){
	//jqgrid全局配置
	$.extend($.jgrid.defaults, {
		ajaxGridOptions : {
			headers: {
				"token": token
			}
		}
	});
}

//权限判断
function hasPermission(permission) {
    if (window.parent.permissions.indexOf(permission) > -1) {
        return true;
    } else {
        return false;
    }
}

//重写alert
window.alert = function(msg, callback){
	parent.layer.alert(msg, function(index){
		parent.layer.close(index);
		if(typeof(callback) === "function"){
			callback("ok");
		}
	});
}

//重写confirm式样框
window.confirm = function(msg, confirmfunc,cancelfunc){
	parent.layer.confirm(msg, {btn: ['确定','取消']},
	function(index, layero){//确定事件
		//console.log('ok');
		if(typeof(confirmfunc) === "function"){
			confirmfunc();
			parent.layer.close(index);
		}
	},function(index, layero){
		//console.log('cancled');
		if(typeof(cancelfunc) === "function"){
			cancelfunc();
		}
	});
}

/*
 * 显示loading遮罩层
 */
function onloading() {
	/*$('<div class="datagrid-mask" id="datagrid-mask"></div>').css({
		display : "block",
		width : "100%",
		height : "100%"
	}).appendTo("body");
	console.log($("#datagrid-mask"));
	$('<div class="datagrid-mask-msg" id="datagrid-mask-msg"></div>').html(
			"正在处理，请稍候。。。").appendTo("body").css({
		display : "block"
	});*/
	var index = layer.load(1);
	return index;
}
function removeload(index) {
	/*$(".datagrid-mask").remove();
	$(".datagrid-mask-msg").remove();*/
	layer.close(index);
}

//选择一条记录
function getSelectedRow() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if(!rowKey){
    	alert("请选择一条记录");
    	return ;
    }
    
    var selectedIDs = grid.getGridParam("selarrrow");
    if(selectedIDs.length > 1){
    	alert("只能选择一条记录");
    	return ;
    }
    
    return selectedIDs[0];
}

//选择多条记录
function getSelectedRows() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if(!rowKey){
    	alert("请选择一条记录");
    	return ;
    }
    
    return grid.getGridParam("selarrrow");
}

//判断是否为空
function isBlank(value) {
    return !value || !/\S/.test(value)
}


//对Date的扩展，将 Date 转化为指定格式的String
//月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符， 
//年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 
//例子： 
//(new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 
//(new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18 
Date.prototype.format = function (fmt) { //author: meizz 
 var o = {
     "M+": this.getMonth() + 1, //月份 
     "d+": this.getDate(), //日 
     "h+": this.getHours(), //小时 
     "m+": this.getMinutes(), //分 
     "s+": this.getSeconds(), //秒 
     "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
     "S": this.getMilliseconds() //毫秒 
 };
 if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
 for (var k in o)
 if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
 return fmt;
}

//不同类型的label
function primaryLabel(v){
	return '<span class="label label-primary">' + v + '</span>';
}
function warningLabel(v){
	return '<span class="label label-warning">' + v + '</span>';
}
function successLabel(v){
	return '<span class="label label-success">' + v + '</span>';
}

//删除对象属性中==null的属性
function removeNull(o){
	for(var i in o){
		if(o[i] == null){
			delete o[i];
		}
	}
	return o;
}

//模拟表单提交同步方式下载文件
// 能够弹出保存文件对话框
function downloadFileByForm(url,params) {
    var form = $("<form></form>").attr("action", url).attr("method", "post");
    for(o in params){
    	form.append($("<input type='hidden' name=" + o + " value=" + params[o] + "></input>"));
    }
    form.append($("<input type='hidden' name='token' value=" + token + "></input>"));
    //form.append($("<input></input>").attr("type", "hidden").attr("name", "fileName").attr("value", fileName));
    form.appendTo('body').submit().remove();
}