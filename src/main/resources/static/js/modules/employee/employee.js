var selected_user_id = 0;
var selected_user_customer_ids = null;

var c_filter = "3";

$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'employee/employee_list?status=1',
        datatype: "json",
        colModel: [			
			{ label: '用户ID', name: 'userId', index: "user_id", width: 45, key: true },
			{ label: '用户名', name: 'username', width: 75 }
        ],
		viewrecords: true,
        height: '100%',
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
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
        onSelectRow: function (rowid, iRow, iCol, e) {
        	//$("#jqGrid").jqGrid("setSelection",rowid);
        	selected_user_id = rowid;
        	vm.selectUser(rowid);
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
    
    $("#jqGrid1").jqGrid({
        url: baseURL + 'customer/list?status=0&c_filter='+c_filter+"&employee_id="+selected_user_id,
        datatype: "json",
        colModel: [	
        	{ label: 'ID', name: 'id', width: 20, key: true, sortable:true},
			{ label: '编号', name: 'customerNo', index: "customer_no", width: 20},
			{ label: '客户名称', name: 'customerName', index: "customer_name", width: 60,sortable:false,formatter: function(value, options, row){
				return "<div class='table-cell'>" + value + "</div>";
			}}
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
        multiselect: true,
        pager: "#jqGridPager1",
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
        	$("#jqGrid1").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        	if(selected_user_customer_ids != null && selected_user_customer_ids.length > 0){
        		$("#jqGrid1").jqGrid('resetSelection');
        		for(var i = 0; i < selected_user_customer_ids.length; i++){
        			$("#jqGrid1").jqGrid('setSelection',selected_user_customer_ids[i]);
        		}
        	}
        }
    });
    
    //绑定check radio事件
    $("input[name='c_filter']").bind('click', function() {
		vm.reloadCustomer1();
	});
});

$.ajaxSetup({
	async: false
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		customerName:null,
		showList:true
	},
	methods: {
		query: function () {
			vm.reloadCustomer1();
		},
		save: function(){
			//所选择的员工
			var ids = $("#jqGrid").jqGrid('getGridParam','selrow');
			//所选择的客户
			var ids1 = $("#jqGrid1").jqGrid('getGridParam','selarrrow');
			//所有客户id
			var ids2 = $("#jqGrid1").jqGrid('getDataIDs');
			console.log("ids=" + ids + ",ids1=" + ids1 + ",ids2=" + ids2);
			if(!ids){
				alert("请选择员工!");
				return;
			}
			/*if(!ids1 || ids1.length <= 0){
				alert("请选择要跟进的客户!");
				return;
			}*/
			var url = baseURL + "employee/save_employee_customer_r?userId=" + ids + "&selCustomerIds=" + ids1.join(",") +"&customerIds=" + ids2.join(",");
			$.ajax({
				type: "POST",
			    url: url,
                contentType: "application/json",
			    //data: {userId:ids,customerIds:ids1},
			    success: function(r){
					if(r.code == 0){
						alert('操作成功', function(){
                            //vm.reload();
							vm.selectUser(ids);
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		reloadEmployee: function () {
			//vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                //postData:{'username': vm.q.username},
                page:page
            }).trigger("reloadGrid");
		},
		reloadCustomer: function () {
			//vm.showList = true;
			var page = $("#jqGrid1").jqGrid('getGridParam','page');
			$("#jqGrid1").jqGrid('setGridParam',{ 
                //postData:{'username': vm.q.username},
                page:page
            }).trigger("reloadGrid");
		},
		reloadCustomer1: function () {
			var c_filter = $('input:radio[name="c_filter"]:checked').val();
			$("#jqGrid1").jqGrid('setGridParam',{ 
				url:baseURL + 'customer/list?c_filter='+c_filter+"&employee_id="+selected_user_id,
                postData:{'status': 0,'customerName':vm.customerName},
				page:1
            }).trigger("reloadGrid");
		},
		selectUser: function(userId){
			this.reloadCustomer1();
			$.ajax({
				type: "GET",
			    url: baseURL + "employee/customer_list?userId=" + userId,
                contentType: "application/json",
			    success: function(r){
					if(r.code == 0){
						console.log(r.list);
						selected_user_customer_ids = r.list;
						$("#jqGrid1").jqGrid('resetSelection');
						for(var i = 0; i < r.list.length; i++){
							$("#jqGrid1").jqGrid('setSelection',r.list[i]);
						}
					}else{
						console.log(r.msg);
					}
				}
			});
		}
	}
});