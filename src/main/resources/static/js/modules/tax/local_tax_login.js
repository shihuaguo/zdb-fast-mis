
//地税登陆 一定要放jquery 初始化方法外面
function localTaxLogin (localTaxAccount,localTaxPwd) {

    if (localTaxAccount==''||localTaxAccount==null){
        alert("地税登录账户不能为空,请确认客户信息！");
        return false;
    }
    if (localTaxPwd==''||localTaxPwd==null){
        alert("地税密码不能为空,请确认客户信息！");
        return false;
    }
    confirm("请确定此时是用【佶佶智能】本地客户端登录",function () {
        var loginInfo =localTaxAccount+","+localTaxPwd;
        var params='{"event":"dslogin","paramInfo":"'+loginInfo+'"}';
        //通过控制台日志监听 给渲染进程通信传递 params消息
        console.log(params)
    })
}


$(function () {

	//检查EptUrl 是否配置
    isSaveEtpUrl();

    //判断是否配置EPT 安装路径地址
    function isSaveEtpUrl(){
        var params='{"event":"isSaveEtpUrl","paramInfo":""}';
        //通过控制台日志监听 给渲染进程通信传递 params消息
        console.log(params);
    }



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
			{ label: '地税登陆账号', name: 'validCode',width: 60,sortable:false, formatter: function(value, options, row){
					var arr = [
	                    "<button onclick=\"localTaxLogin('",row.customerTax ? row.customerTax.legalPersonAccount:'',"','",
	                    		row.customerTax ? row.customerTax.legalPersonPassword:'',
	                    "');\" ",
	                    "type='button'>登录</button>"
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