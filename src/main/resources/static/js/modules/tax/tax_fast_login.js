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
//阿里云滑动验证码-验证处理，完成后登陆到税务网
function  aliyunCaptchCode(id,legalPersonAccount,legalPersonPassword,customerName,taxIdNumber) {
    <!-- 此段必须要引入 -->
    var ieVersion = 11;
    var  browerName ="chrome";
    if(browerName!="IE" || ieVersion>=9){
        var nc = new noCaptcha();
        var nc_appkey = "FFFF0000000001771572";  // 应用标识,不可更改
        var nc_scene = 'login';  //场景,不可更改
        var nc_token = [nc_appkey, (new Date()).getTime(), Math.random()].join(':');
        var nc_option = {
            renderTo: '#dom_id',//渲染到该DOM ID指定的Div位置
            appkey: nc_appkey,
            scene: nc_scene,
            token: nc_token,
            trans: '{"name1":"code100"}',//测试用，特殊nc_appkey时才生效，正式上线时请务必要删除；code0:通过;code100:点击验证码;code200:图形验证码;code300:恶意请求拦截处理
            callback: function (data) {// 校验成功回调
                document.getElementById('csessionid').value = data.csessionid;
                document.getElementById('sig').value = data.sig;
                document.getElementById('token').value = nc_token;
                document.getElementById('scene').value = nc_scene;
                setTimeout(frzhlogin(id,legalPersonAccount,legalPersonPassword,customerName,taxIdNumber),1500);
            }
        };
        nc.init(nc_option);

    }
    <!-- 引入结束 -->
}
// 初始化 阿里云滑动验证码
function  initAliyunCaptchCode(id,legalPersonAccount,legalPersonPassword,customerName,taxIdNumber){

    aliyunCaptchCode(id,legalPersonAccount,legalPersonPassword,customerName,taxIdNumber);
}
//法人账号登录
function frzhlogin(id,legalPersonAccount,legalPersonPassword,customerName,taxIdNumber) {

    if (!legalPersonAccount){
    	alert("法人账号不能为空");
        return false;
    }

    if (!legalPersonPassword){
    	alertr("法人密码信息数据库未找到,请核对");
        return false;
    }


    legalPersonPassword = encode(legalPersonPassword);
    // $("#captchCode").val(captchCode);
	$("#userNameOrSjhm").val(legalPersonAccount);
	$("#passWord").val(legalPersonPassword);
	$("#shxydmOrsbh").val(taxIdNumber);

    console.log($("#shxydmOrsbh").val());
    console.log($("#userNameOrSjhm").val());
    console.log($("#passWord").val());


	var urlPre = "https://www.etax-gd.gov.cn";
	var ssoLogin = function(callback,callback1,callback2){
		var url = urlPre + '/sso/login?service=https://www.etax-gd.gov.cn/xxmh/html/index.html?bszmFrom=1&t=1511852007328';
		//登陆
		jQuery.ajax({
			type: "post",
			async: false,
			url: url,
			data: jQuery('#upLoginForm').serialize(),
			dataType: "jsonp",
			jsonp: "callback",//传递给请求处理程序或页面的，用以获得jsonp回调函数名的参数名(一般默认为:callback)
			jsonpCallback:"flightHandler1",//自定义的jsonp回调函数名称，默认为jQuery自动生成的随机函数名，也可以写"?"，jQuery会自动为你处理数据
			success: function(json){
                console.log("ssoLogin success!");
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
                console.log("checkLoginState success!");
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
		//   用户身份
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
    	window.open(urlPre + "/sso/login?service=https://www.etax-gd.gov.cn/xxmh/html/index.html?bszmFrom=1&t=1511852007328#none");
    }

    //获取用户的checkLoginState
	var qybdid = null;
	$.get(baseURL + 'customer/checkLoginState?customerId='+id, function(r){
		if(r && r.code == 0){
			//console.log(r.data);
			if(r.data == null || $.trim(r.data) == ''){
				alert("客户信息没有同步checkLoginState,请重新同步");
				return;
			}
			qybdid = null;
			var nsrQysqVos = eval('(' + r.data + ')').nsrQysqVos;
			for(var i = 0; i < nsrQysqVos.length; i++){
				if(nsrQysqVos[i].zzNsrmc == customerName){
					qybdid = nsrQysqVos[i].qybdid;
					console.log("customerName=" + customerName + ", qybdid=" + qybdid);
					break;
				}
			}
			if(qybdid){
				ssoLogin(checkLoginState, changeSf, openEtax);
			}
		}else {
			alert("获取客户checkLoginState信息失败");
		}
	});

    /*if(qybdid != null){
    	ssoLogin(checkLoginState, changeSf, openEtax);
    }*/
};

$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'customer/listWithTax',
        datatype: "json",
        colModel: [
        	{ label: 'ID', name: 'id', width: 20, key: true, sortable:true, hidden:true},
			{ label: '编号', name: 'customerNo', index: "customer_no", width: 20,sortable:true},
			{ label: '统一代码', name: 'taxIdNumber', index: "taxIdNumber", width: 40,sortable:true},
			{ label: '客户名称', name: 'customerName', index: "customer_name", width: 60,sortable:false,formatter: function(value, options, row){
				//return "<div class='table-cell'>" + value + "</div>";
				return "<div class='table-cell'>" + value + "</div>";
			}},
			{ label: '法人账号登录', name: 'validCode',width: 60,sortable:false, formatter: function(value, options, row){
					var arr = [
	                    "<button onclick=\"initAliyunCaptchCode(",
	                    row.id,",'",
                        row.customerTax?row.customerTax.legalPersonAccount:null,"','",
	                    row.customerTax?row.customerTax.legalPersonPassword:null,"','",
                        row.customerName,"','",
                        row.taxIdNumber,"'",
	                    ");\" ",
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
            customerNameOrNo: null,
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
                postData:{'customerNameOrNo': vm.q.customerNameOrNo},
                page:page
            }).trigger("reloadGrid");
		},
        validator: function () {
        	return false;
        }
	}
});
