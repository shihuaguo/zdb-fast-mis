var status = T.p("status");
if(status == null){
	status = 0;
}

$(function () {
	//getCustomerTypes();
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
			{ label: '客户类别', name: 'customerType', index: "customer_type", width: 20},
			{ label: '区域', name: 'region',index: "region",  width: 30,sortable:false},
			{ label: '异常信息', name: 'customerIndCom.abnormalList',width: 40,sortable:false,formatter: function(value, options, row){
				if(value){
					var el = document.createElement('html');
					//是否被移除
					var isMoved = false;
					try{
						el.innerHTML = value;
						var tbody = el.getElementsByTagName('tbody');
						if(tbody && tbody.length > 0){
							var trs = tbody[0].children;
							if(trs && trs.length > 0){
								for(var i = 0; i < trs.length; i++){
									var tds = trs[i].children;
									if(tds && tds.length >= 5 && tds[4].innerHTML){
										isMoved = true;
									}else {
										isMoved = false;
									}
								}
							}
						}else {
							return "暂无信息";
						}
					}catch(e){
						console.log(e);
					}
					return isMoved ? "异常信息(移除)" : "<span style='color:red;'>异常信息</span>";
				}
				return value ? (value.indexOf('暂无信息') > -1 ? "暂无信息" : "<span style='color:red;'>异常信息</span>") : "-";
			}},
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
            customerNameOrNo: null,
			customerType:null
		},
		showList: true,			//显示列表
		isSyncIc: false,		//是否同步了工商信息
		isSyncTax: false,		//是否同步了税务信息
		title:null,
		customer:{},			//客户信息
		customerIc:{},			//工商登记信息
		customerTax:{},			//工商登记信息
		customerCri:{},			//商事主体信息
		customerList:{},
		customerTypes:[],		//客户类型枚举
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
			vm.customerCri = {};
			vm.customerList = {};
			this.getValidCodeOfIc();
			this.getValidCodeOfTax();
			vm.isSyncIc = false;
			vm.isSyncTax = false;
			$("#AbnormalList").html('');
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
		saveOrUpdateCri: function () {
            if(!vm.customer.id){
            	alert("请先保存工商或税局信息")
                return ;
            }
			var url = "customer/updateCri";
			vm.customerCri.customerId = vm.customer.id;
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/x-www-form-urlencoded",
			    data: vm.customerCri,
			    success: function(r){
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
					vm.customerCri = {};
					vm.customerCri.businessStatus = r.customerIc.businessStatus;
					if(r.customerIc.abnormalList){
						vm.customerCri.abnormalList = r.customerIc.abnormalList;
						$("#AbnormalList").html(r.customerIc.abnormalList);
					}else {
						$("#AbnormalList").html('');
					}
				}
				if(r.customerTax){
					vm.customerTax = r.customerTax;
				}
			});
		},
		//获取工商登记信息验证码
		getValidCodeOfIc: function(){
			$("#validImg").click();
			$("#validImg2").click();
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


		syncTax: function(csessionid,sig,token,scene){
			if($.trim(csessionid) == ""){
				alert("滑动验证码csessionid不能为空");
				return;
			}
			if($.trim(sig) == ""){
				alert("滑动验证码签名不能为空");
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
				 + "&legalPersonPassword="+vm.customerTax.legalPersonPassword+"&customerName=" + vm.customer.customerName
				 + "&csessionid="+csessionid+"&sig=" + sig+"&token=" + token+"&scene=" + scene;
			urlTax = urlTax + "&customerId=" + vm.customer.id;
			var index = onloading();
			
			$.ajax({
				url:urlTax,
				//data:$("#checkForm").serialize(),
				dataType:'json',
				success:function(r){
					removeload(index);
					if(r.code == 0){
						var ct = $.extend({}, vm.customerTax, removeNull(r.customerTax));
						/*ct.bsrxm 	= r.customerTax.bsrxm;
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
						ct.ckzhzh = r.customerTax.ckzhzh;
						ct.checkLoginState = r.customerTax.checkLoginState;*/
						if(ct.ticketAgent == null || ct.ticketAgent == ''){
							ct.ticketAgent = '无';
						}
						//vm.customerTax = $.extend({}, vm.customerTax, ct);
						vm.customerTax = ct;
						//处理开户银行和账号
						if(ct.ckzhzh){
							var ckzhzh = eval('(' + ct.ckzhzh + ')');
							//console.log(ckzhzh);
							var khyh = '', zh = '';
							for(var i = 0; i < ckzhzh.length; i++){
								khyh = khyh + ckzhzh[i].gdslx + ":" + ckzhzh[i].khyh + ",";
								zh = zh + ckzhzh[i].gdslx + ":" + ckzhzh[i].zh+ ",";
							}
							vm.customer.bankName = khyh;
							vm.customer.bankAccount = zh;
						}
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
		syncCri: function(){
			var validCode = $("#validCode2").val();
			if($.trim(validCode) == ""){
				alert("请输入验证码");
				return;
			}
			if($.trim(vm.customerIc.socialReditOde) == ""){
				alert("请输入社会信用代码");
				return;
			}
			//查询工商登记信息的地址
			var urlCri = baseURL + "customer/sync/cri?socialReditOde=" + vm.customerIc.socialReditOde 
						+ "&validCode=" + validCode + "&guid=" + cri_guid;
			var index = onloading();
			$.ajax({
                url:urlCri,
                dataType:'json',
                success:function(r){
                	removeload(index);
                    if(r.code == 0){
                    	vm.customerCri = $.extend({}, vm.customerCri, r.customerCri);
                    	if(r.customerCri.AbnormalList){
                    		$("#AbnormalList").html(r.customerCri.AbnormalList);
                    	}
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

                    postData:vm.q,
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
		getCustomerTypes: function(){
			$.get(baseURL + 'sys/config/list', {'key':'customer_type','page':1,'limit':10}, function(r){
				console.log(r);
				if(r.code === 0 && r.page && r.page.list && r.page.list.length > 0){
					var v = r.page.list[0].value;
					vm.customerTypes = v.split(",");
				}
			});
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

vm.getCustomerTypes();

//var customer_types = [];

/*function getCustomerTypes(){
	$.get(baseURL + 'sys/config/list', {'key':'customer_type','page':1,'limit':10}, function(r){
		console.log(r);
		if(r.code === 0 && r.page && r.page.list && r.page.list.length > 0){
			var v = r.page.list[0].value;
			customer_types = v.split(",");
		}
	});
}*/

vm.status = status;