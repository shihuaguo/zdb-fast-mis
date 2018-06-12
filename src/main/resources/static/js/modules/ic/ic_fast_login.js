
function refreshAuthCode(img){
	img.attr('src','http://www.gzaic.gov.cn/gzaic/App/AuthCode.html?'+Math.random());
}

function gsjUpLogin(id,taxIdNumber,industryCommercePwd) {
	var captchCode=$("#gscaptcha_"+id).val();
	if (captchCode==''||captchCode==null){
        alert("验证码不能为空");
        return false;
	}

    if (taxIdNumber==''||taxIdNumber==null){
    	alert("社会信用代码不能为空");
        return false;
    }

    if (industryCommercePwd==''||industryCommercePwd==null){
    	alertr("密码信息数据库未找到,请核对");
        return false;
    }

    $("#yzm").val(captchCode);
    $("#account").val(taxIdNumber);
    $("#password").val(industryCommercePwd);
    //var url = 'http://www.gzaic.gov.cn/gzaic/App/Login.html';
    $('#myform').submit();
};

$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'customer/listWithIc',
        datatype: "json",
        colModel: [	
        	{ label: 'ID', name: 'id', width: 20, key: true, sortable:true},
			{ label: '编号', name: 'customerNo', index: "customer_no", width: 20},
			{ label: '客户名称', name: 'customerName', index: "customer_name", width: 60,sortable:false,formatter: function(value, options, row){
				//return "<div class='table-cell'>" + value + "</div>";
				return "<div class='table-cell'>" + value + "</div>";
			}},
			{ label: '验证码', name: 'validCode',width: 60,sortable:false, formatter: function(value, options, row){
					var arr = [
						"<img id='yzmimg_",
						row.id,
						"' ",
						"src='http://www.gzaic.gov.cn/gzaic/App/AuthCode.html?0.5735559373659491'",
						"width='70'",
	                    "height='22' onclick='refreshAuthCode($(this))'>",
	                    "<input id='gscaptcha_",
	                    row.id,
						"' ",
	                    "type='text' style='width: 70px; height: 25px;' required>",
	                    "<button onclick=\"gsjUpLogin(",
	                    row.id,",'",row.taxIdNumber,"','",row.customerIndCom ? row.customerIndCom.industryCommercePwd : null,
	                    "');\" ",
	                    "type='button' class='btn btn-default'>登录工商局</button>"
					];
					return arr.join("");
			}},
			{ label: '统一代码', name: 'taxIdNumber', index: "tax_id_number", width: 50,sortable:false,formatter: function(value, options, row){
				//return "<div class='table-cell'>" + value + "</div>";
				return "<div class='table-cell'>" + value + "</div>";
			}},
			{ label: '工商登录密码', name: 'customerIndCom.industryCommercePwd', width: 40,sortable:false,hidden:true},
			{ label: '法人', name: 'legalPerson', index: "legal_person", width: 40,sortable:false}
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
			customerName: null
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
                postData:{'customerName': vm.q.customerName},
                page:page
            }).trigger("reloadGrid");
		},
        validator: function () {
        	return false;
        }
	}
});