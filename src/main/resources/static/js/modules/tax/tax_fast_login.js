$.ajaxSetup({
	//dataType: "json",
	cache: false,
	async: false,
    /*headers: {
        "token": token
    },*/
    /*xhrFields: {
	    withCredentials: true
    },*/
    complete: function(xhr) {
        /*if(xhr.responseJSON.code == 401){
            parent.location.href = baseURL + 'login.html';
        }*/
    	//console.log(xhr);
    }
});

//获取所有cookie的key
function getCookieKeys(){
	var arr = document.cookie.split(";");
	var keys = [];
	if(arr && arr.length > 0){
		for(var i = 0; i < arr.length; i++){
			keys.push(arr[i].split("=")[0]);
		}
	}
	return keys;
}

function refreshAuthCode(img){
	img.attr('src','http://www.etax-gd.gov.cn/sso/base/captcha.do?r='+Math.random());
}

//法人账号登录
function frzhlogin(id,legalPersonAccount,legalPersonPassword,customerName) {
	var captchCode=$("#fr_captcha_"+id).val();
	if (captchCode==''||captchCode==null){
        alert("验证码不能为空");
        return false;
	}

    if (legalPersonAccount==''||legalPersonAccount==null){
    	alert("法人账号不能为空");
        return false;
    }

    if (legalPersonPassword==''||legalPersonPassword==null){
    	alertr("法人密码信息数据库未找到,请核对");
        return false;
    }

    legalPersonPassword = encode(legalPersonPassword);
    $("#captchCode").val(captchCode);
	$("#userName").val(legalPersonAccount);
	$("#passWord").val(legalPersonPassword);
    //var url = 'http://www.gzaic.gov.cn/gzaic/App/Login.html';
    //$('#myform').submit();
	
	//获取用户的checkLoginState
	var qybdid = null;
	$.get(baseURL + 'customer/checkLoginState?customerId='+id, function(r){
		if(r && r.code == 0){
			console.log(r.data);
			if(r.data == null || $.trim(r.data) == ''){
				alert("客户信息没有同步checkLoginState,请重新同步");
				return;
			}
			var nsrQysqVos = eval('(' + r.data + ')').nsrQysqVos;
			for(var i = 0; i < nsrQysqVos.length; i++){
				if(nsrQysqVos[i].zzNsrmc == customerName){
					qybdid = nsrQysqVos[i].qybdid;
					console.log("customerName=" + customerName + ", qybdid=" + qybdid);
					break;
				}
			}
		}else {
			alert("获取客户checkLoginState信息失败");
		}
	});
	
	var urlPre = "http://www.etax-gd.gov.cn";
	
	var ssoLogin = function(callback,callback1,callback2){
		var url = urlPre + '/sso/login?service=http://www.etax-gd.gov.cn/xxmh/html/index.html?bszmFrom=1&amp;t=1502697830856';
		//登陆
		jQuery.ajax({
			type: "get",
			async: false,
			url: url,
			data: jQuery('#upLoginForm').serialize(),
			dataType: "jsonp",
			jsonp: "callback",//传递给请求处理程序或页面的，用以获得jsonp回调函数名的参数名(一般默认为:callback)
			jsonpCallback:"flightHandler1",//自定义的jsonp回调函数名称，默认为jQuery自动生成的随机函数名，也可以写"?"，jQuery会自动为你处理数据
			success: function(json){
			},
			complete: function(xhr) {
				console.log("ssoLogin complete!");
				callback(callback1,callback2);
		    },
			error: function(xhr,error,e){
				console.log("ssoLogin error!");
			}
		});
	}
	
	var checkLoginState = function(callback1,callback2){
		//  登陆验证
		var url = urlPre + '/sso/auth/checkLoginState.do';
		jQuery.ajax({
			type: "get",
			async: false,
			url: url,
			//data: jQuery('#upLoginForm').serialize(),
			dataType: "jsonp",
			jsonp: "callback",//传递给请求处理程序或页面的，用以获得jsonp回调函数名的参数名(一般默认为:callback)
			jsonpCallback:"flightHandler",//自定义的jsonp回调函数名称，默认为jQuery自动生成的随机函数名，也可以写"?"，jQuery会自动为你处理数据
			success: function(json){
			},
			complete: function(xhr) {
				console.log("checkLoginState complete!");
				callback1(callback2);
		    },
			error: function(xhr,error,e){
			}
		});
	}
	
	// 用户身份
	var changeSf = function(callback){
		//  登陆验证
		var url = urlPre + '/sso/auth/changeQySf.do';
		jQuery.ajax({
			type: "get",
			async: false,
			url: url,
			data: {"qybdid" : qybdid},
			dataType: "jsonp",
			jsonp: "j",//传递给请求处理程序或页面的，用以获得jsonp回调函数名的参数名(一般默认为:callback)
			jsonpCallback:"",//自定义的jsonp回调函数名称，默认为jQuery自动生成的随机函数名，也可以写"?"，jQuery会自动为你处理数据
			success: function(json){
			},
			complete: function(xhr) {
				console.log("changeSf complete!");
				callback();
		    },
			error: function(xhr,error,e){
				console.log("changeSf error,error=" + error + ",e=" + e);
			}
		});
	}
    
    var openEtax = function(){
    	//window.open("http://www.etax-gd.gov.cn/xxmh/html/index.html?bszmFrom=1&ticket=ST-640015-lw5beAOZ5QR9krlJgTyV-gddzswj");
    	window.open(urlPre + "/sso/login?service=http://www.etax-gd.gov.cn/xxmh/html/index.html?bszmFrom=1&t=1511852007328#none");
    }
    
    if(qybdid != null){
    	ssoLogin(checkLoginState, changeSf, openEtax);
    }
    
	/*//var urlPre = baseURL;
	var urlPre = "";
	//改为通过nginx代理去执行
    var ssoLogin = function(callback,callback1,callback2){
		var url = urlPre + '/etax/sso/login?service=http://www.etax-gd.gov.cn/xxmh/html/index.html?bszmFrom=1&amp;t=1502697830856';
		//登陆
		jQuery.ajax({
			type: "post",
			async: false,
			url: url,
			data: jQuery('#upLoginForm').serialize(),
			dataType: "html",
			success: function(json){
				console.log("ssoLogin,json=" + json);
				if(json.indexOf("CAS登录成功") > 0){
					if(callback && callback1 && callback2){
						callback(callback1,callback2);
					}
				}else {
					alert("登录税局失败");
				}
			},
			error: function(xhr,error,e){
				console.log("ssoLogin error,error=" + error + ",e=" + e);
			}
		});
	}
	
	var checkLoginState = function(callback1,callback2){
		//  登陆验证
		var url = urlPre + '/etax/sso/auth/checkLoginState.do';
		jQuery.ajax({
			type: "post",
			async: false,
			url: url,
			//data: jQuery('#upLoginForm').serialize(),
			dataType: "json",
			success: function(json){
				console.log("checkLoginState,json=" + json);
				if(json.flag == "ok"){
					//找到正确的qybdid
					var qybdid = null;
					var nsrQysqVos = json.nsrQysqVos;
					for(var i = 0; i < nsrQysqVos.length; i++){
						if(nsrQysqVos[i].zzNsrmc == customerName){
							qybdid = nsrQysqVos[i].qybdid;
							console.log("customerName=" + customerName + ", qybdid=" + qybdid);
							break;
						}
					}
					if(callback1 && callback2 && qybdid != null){
						callback1(callback2, qybdid);
					}
				}
			},
			error: function(xhr,error,e){
				console.log("checkLoginState error,error=" + error + ",e=" + e);
			}
		});
	}
	
	// 用户身份
	var changeSf = function(callback, qybdid){
		//  登陆验证
		var url = urlPre + '/etax/sso/auth/changeQySf.do';
		jQuery.ajax({
			type: "post",
			async: false,
			url: url,
			data: {"qybdid" : qybdid},
			dataType: "json",
			success: function(json){
				console.log("changeSf,json=" + json);
				if(json.flag == "ok"){
					if(callback){
						callback();
					}
				}
			},
			error: function(xhr,error,e){
				console.log("changeSf error,error=" + error + ",e=" + e);
			}
		});
	}
    
    var openEtax = function(){
    	window.open("http://www.etax-gd.gov.cn/xxmh/html/index.html?bszmFrom=1&ticket=ST-640015-lw5beAOZ5QR9krlJgTyV-gddzswj");
    	//window.open("http://www.etax-gd.gov.cn/sso/login?service=http://www.etax-gd.gov.cn/xxmh/html/index.html?bszmFrom=1&t=1511852007328#none");
    }
    
    ssoLogin(checkLoginState, changeSf, openEtax);*/
};

$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'customer/listWithTax',
        datatype: "json",
        colModel: [	
        	{ label: 'ID', name: 'id', width: 20, key: true, sortable:true, hidden:true},
			{ label: '编号', name: 'customerNo', index: "customer_no", width: 20,sortable:true},
			{ label: '客户名称', name: 'customerName', index: "customer_name", width: 60,sortable:false,formatter: function(value, options, row){
				//return "<div class='table-cell'>" + value + "</div>";
				return "<div class='table-cell'>" + value + "</div>";
			}},
			{ label: '法人账号登录', name: 'validCode',width: 60,sortable:false, formatter: function(value, options, row){
					var arr = [
						"<img id='yzmimg_",
						row.id,
						"' ",
						"src='http://www.etax-gd.gov.cn/sso/base/captcha.do?r='",
						"width='70'",
	                    "height='22' onclick='refreshAuthCode($(this))'>",
	                    "<input id='fr_captcha_",
	                    row.id,
						"' ",
	                    "type='text' style='width: 70px; height: 25px;' value='1' required>",
	                    "<button onclick=\"frzhlogin(",
	                    row.id,",'",row.customerTax?row.customerTax.legalPersonAccount:null,"','",
	                    		row.customerTax?row.customerTax.legalPersonPassword:null,"','",row.customerName,
	                    "');\" ",
	                    "type='button' class='btn btn-default'>登录</button>"
					];
					return arr.join("");
			}},
			{ label: '购票员', name: 'customerTax.ticketAgent', width: 30,sortable:false},
			{ label: '法人', name: 'customerTax.fddbrxm', width: 30,sortable:false},
			{ label: '财务负责人', name: 'customerTax.cwfzrxm', width: 30,sortable:false},
			{ label: '办税员', name: 'customerTax.bsrxm', width: 30,sortable:false}
        ],
		viewrecords: true,
        height: '100%',
        rowNum: 20,
		rowList : [20,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        sortname: 'customer_no',
        sortorder: 'asc',
        autowidth:true,
        multiselect: false,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			customerName: null,
			customerNo:null
		},
		showList: true,			//显示列表
		title:null,
		customer:{},			//客户信息
		customerIc:{},			//工商登记信息
		customerTax:{},			//工商登记信息
		customerList:{}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		getCustomer: function(id){
			$.get(baseURL + "customer/getById/"+id, function(r){
				vm.customer = r.customer;
				if(r.customerIc){
					vm.customerIc = r.customerIc;
				}
				if(r.customerTax){
					vm.customerTax = r.customerTax;
					//vm.bindingGsDs(vm.customerTax.gs,vm.customerTax.ds);
				}
			});
		},
		getUserList: function(){
			$.get(baseURL + "sys/user/listAll", function(r){
				vm.userList = r.list;
			});
		},
		reload: function () {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                postData:{'customerName': vm.q.customerName,'customerNo':vm.q.customerNo},
                page:page
            }).trigger("reloadGrid");
		},
        validator: function () {
        	return false;
        }
	}
});