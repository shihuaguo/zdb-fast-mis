var status = T.p("status");
if(status == null){
	status = 0;
}

$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'customer/list?status='+status,
        datatype: "json",
        colModel: [	
        	{ label: 'ID', name: 'id', width: 20, key: true, sortable:true},
			{ label: '编号', name: 'customerNo', index: "customer_no", width: 20},
			{ label: '客户名称', name: 'customerName', index: "customer_name", width: 60,sortable:false,formatter: function(value, options, row){
				//return "<div class='table-cell'>" + value + "</div>";
				return "<div class='table-cell'>" + value + "</div>";
			}},
			{ label: '区域', name: 'region',index: "region",  width: 30,sortable:false},
			{ label: '统一代码', name: 'taxIdNumber', index: "tax_id_number", width: 40,sortable:false,formatter: function(value, options, row){
				//return "<div class='table-cell'>" + value + "</div>";
				return "<div class='table-cell'>" + value + "</div>";
			}},
			{ label: '法人', name: 'legalPerson', index: "legal_person", width: 30,sortable:false},
			{ label: '法人身份证号', name: 'legalPersonId', index: "legal_person_id", width: 40,sortable:false},
			{ label: '开户银行', name: 'bankName', width: 40,sortable:false},
			{ label: '账号', name: 'bankAccount', width: 40,sortable:false},
			{ label: '法人账号', name: 'customerTax.legalPersonAccount', width: 30,sortable:false},
			{ label: '法人密码', name: 'customerTax.legalPersonPassword', width: 40,sortable:false}
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
        ondblClickRow: function (rowid, iRow, iCol, e) {
        	//console.log(rowid);
        	//console.log(iRow);
        	//console.log(iCol);
        	//console.log(e);
        	$("#jqGrid").jqGrid("setSelection",rowid);
        	//this.setSelection(rowid);
        	vm.update();
        },
        //subGrid : true,
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
    
    $("#jqGrid").jqGrid('navGrid','#jqGridPager',{add:false,edit:false,del:false,search:false,refresh:false});
    $("#jqGrid").jqGrid('navButtonAdd','#jqGridPager',{
        caption: "选择列",
        title: "自定义列",
        onClickButton : function (){
            jQuery("#jqGrid").jqGrid('columnChooser');
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
		isSyncIc: false,		//是否同步了工商信息
		isSyncTax: false,		//是否同步了税务信息
		title:null,
		customer:{},			//客户信息
		customerIc:{},			//工商登记信息
		customerTax:{},			//工商登记信息
		customerList:{},
		status:0
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增客户";
			vm.customer = {};
			vm.customerIc = {};
			vm.customerTax = {};
			vm.customerList = {};
			this.getValidCodeOfIc();
			this.getValidCodeOfTax();
			vm.isSyncIc = false;
			vm.isSyncTax = false;
		},
		update: function () {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改客户";
            this.getValidCodeOfIc();
            this.getValidCodeOfTax();
            this.getCustomer(id);
            vm.isSyncIc = false;
			vm.isSyncTax = false;
		},
		del: function () {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			confirm('确定要删除选中的记录?', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "customer/delete",
                    contentType: "application/json",
				    data: JSON.stringify(ids),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(){
                                vm.reload();
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		saveOrUpdateIc: function () {
            if(vm.validator()){
                return ;
            }
			var url = vm.customer.id == null ? "customer/saveIc" : "customer/update";
			vm.customer.customerIndCom = vm.customerIc;
			//vm.customer.customerTax = vm.customerTax;
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.customer),
			    success: function(r){
			    	vm.isSyncIc = false;
					vm.isSyncTax = false;
			    	if(r.code === 0){
						alert('操作成功', function(){
							//vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		saveOrUpdateTax: function () {
			if(vm.validator()){
				return ;
			}
			var url = vm.customer.id == null ? "customer/saveTax" : "customer/update";
			vm.customer.customerTax = vm.customerTax;
			$.ajax({
				type: "POST",
				url: baseURL + url,
				contentType: "application/json",
				data: JSON.stringify(vm.customer),
				success: function(r){
					vm.isSyncIc = false;
					vm.isSyncTax = false;
					if(r.code === 0){
						alert('操作成功', function(){
							//vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
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
		//获取工商登记信息验证码
		getValidCodeOfIc: function(){
			$("#validImg").click();
		},
		getValidCodeOfTax: function(){
			$("#validImg1").click();
		},
		syncIc: function(){
			var validCode = $("#validCode").val();
			console.log(validCode);
			if($.trim(validCode) == ""){
				alert("请输入验证码");
				return;
			}
			if($.trim(vm.customerIc.socialReditOde) == ""){
				alert("请输入社会信用代码");
				return;
			}
			//查询工商登记信息的地址
			var urlIc = baseURL + "customer/sync/ic?socialReditOde=" + vm.customerIc.socialReditOde + "&validCode=" + validCode;
			var index = onloading();
			$.ajax({
                url:urlIc,
                //data:$("#checkForm").serialize(),
                dataType:'json',
                success:function(r){
                	removeload(index);
                    if(r.code == 0){
                    	var customerId = vm.customer.id;
                    	vm.customer = $.extend({}, vm.customer, removeNull(r.customer));
                    	//vm.customer.customerName = r.customer.customerName;
                    	//vm.customer.legalPerson = r.customer.legalPerson;
                    	vm.customerIc = $.extend({}, vm.customerIc, removeNull(r.customerIc));
                    	vm.customer.id = customerId;
                    	vm.isSyncIc = true;
                    	//自动提取区域信息
                    	var region_p = /天河|越秀|荔湾|海珠|黄埔|白云|番禺|增城|从化|花都/i;
                    	if(r.customerIc.registerAddr != null){
                    		var region = region_p.exec(r.customerIc.registerAddr);
                    		if(region != null){
                    			vm.customer.region = region[0];
                    		}
                    	}
                    	alert("同步成功");
                    	//console.log(r.data);
                    }else{
                        alert(r.msg? r.msg : "未知错误");
                    }
                },
                error:function(){
                	removeload(index);
                    alert("同步出现错误");
                }
            })
		},
		syncTax: function(){
			var validCode = $("#validCode1").val();
			console.log(validCode);
			if($.trim(validCode) == ""){
				alert("请输入验证码");
				return;
			}
			if($.trim(vm.customerTax.legalPersonAccount) == ""){
				alert("请输入法人实名账号");
				return;
			}
			if($.trim(vm.customerTax.legalPersonPassword) == ""){
				alert("请输入法人账号密码");
				return;
			}
			if($.trim(vm.customer.customerName) == ""){
				alert("请输入客户名称");
				return;
			}
			//查询工商登记信息的地址
			//var urlIc = "http://gsxt.gzaic.gov.cn/aiccips/GZpublicity/checkCodeGz.html?entName=&regNo=&text3=" + vm.customerIc.socialReditOde + "code=" + validCode;
			var urlTax = baseURL + "customer/sync/tax?legalPersonAccount=" + vm.customerTax.legalPersonAccount 
				+ "&validCode=" + validCode + "&legalPersonPassword="+vm.customerTax.legalPersonPassword+"&customerName=" + vm.customer.customerName;
			urlTax = urlTax + "&customerId=" + vm.customer.id;
			var index = onloading();
			
			$.ajax({
				url:urlTax,
				//data:$("#checkForm").serialize(),
				dataType:'json',
				success:function(r){
					removeload(index);
					if(r.code == 0){
						var ct = {};
						ct.bsrxm 	= r.customerTax.bsrxm;
						ct.bsryddh 	= r.customerTax.bsryddh;
						ct.bsrzjhm 	= r.customerTax.bsrzjhm;
						ct.cwfzrxm 	= r.customerTax.cwfzrxm;
						ct.cwfzryddh = r.customerTax.cwfzryddh;
						ct.cwfzrzjhm = r.customerTax.cwfzrzjhm;
						ct.fddbrxm 	= r.customerTax.fddbrxm;
						ct.fddbryddh = r.customerTax.fddbryddh;
						ct.fddbrzjhm = r.customerTax.fddbrzjhm;
						ct.nationalTaxNumber = r.customerTax.nationalTaxNumber;
						ct.nationalTaxNumber = r.customerTax.nationalTaxNumber;
						ct.nationalTaxDpt = r.customerTax.nationalTaxDpt;
						ct.localTaxDpt = r.customerTax.localTaxDpt;
						ct.ticketAgent = r.customerTax.ticketAgent;
						if(ct.ticketAgent == null || ct.ticketAgent == ''){
							ct.ticketAgent = '无';
						}
						ct.checkLoginState = r.customerTax.checkLoginState;
						vm.customerTax = $.extend({}, vm.customerTax, ct);
						vm.isSyncTax = true;
						//法人身份证号同步至工商信息的法人身份证号
						vm.customer.legalPersonId = ct.fddbrzjhm;
						alert("同步成功");
					}else{
						alert(r.msg? r.msg : "未知错误");
					}
				},
				error:function(){
					removeload(index);
					alert("同步出现错误");
				}
			})
		},
		getUserList: function(){
			$.get(baseURL + "sys/user/listAll", function(r){
				vm.userList = r.list;
			});
		},
		reload: function () {
			var toList = function(){
				vm.isSyncIc = false;
				vm.isSyncTax = false;
				vm.showList = true;
				var page = $("#jqGrid").jqGrid('getGridParam','page');
				$("#jqGrid").jqGrid('setGridParam',{ 
					postData:{'customerName': vm.q.customerName,'customerNo':vm.q.customerNo},
					page:page
				}).trigger("reloadGrid");
			};
			if(this.isSyncIc || this.isSyncTax){
				var msg = this.isSyncIc ? "工商信息同步未保存,确定返回码?":"税务信息同步未保存,确定返回码?";
				confirm(msg,toList);
			}else {
				toList();
			}
		},
		exportExcel: function(){
			//$.post(baseURL + "customer/export",{'customerName': vm.q.customerName,'customerNo':vm.q.customerNo,'page':1,'limit':1000},function(r){});
			var params = {'page':1,'limit':1000,'status':0};
			if(vm.q.customerName){
				params.customerName = vm.q.customerName;
			}
			if(vm.q.customerNo){
				params.customerNo = vm.q.customerNo;
			}
			downloadFileByForm(baseURL + "customer/export",params);
		},
        validator: function () {
        	return false;
        }
	}
});

vm.status = status;