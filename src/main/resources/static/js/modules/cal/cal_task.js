function primaryLabel(v){
	return '<span class="label label-primary">' + v + '</span>';
}
function warningLabel(v){
	return '<span class="label label-warning">' + v + '</span>';
}
function successLabel(v){
	return '<span class="label label-success">' + v + '</span>';
}

function switchButton(v,o,r){
	var arr = [];
	arr.push("<input type='checkbox' class='my-switch' ");
	arr.push("id='switch_");
	arr.push(r.id);
	arr.push("' ");
	arr.push("data-on-text='未完成' data-off-text='已完成'");
	if(r.status == 0){
		arr.push(" checked ")
	}
	arr.push("/>");
	return arr.join("");
}

//事项中日期输入校验
function dateCheck(value, colname) {
	if (!isValidDateWithHH(value)) 
	   return [false,"请输入正确的日期格式(yyyy-MM-dd hh)"];
	else 
	   return [true,""];
}

var status = T.p("status");

$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'cal/task/list?status='+status,
        datatype: "json",
        colModel: [	
        	{ label: 'ID', name: 'id', width: 10, key: true, hidden: true },
        	{ label: '状态', name: 'status', width: 20, formatter: function(value, options, row){
				return value == 0 ? warningLabel('未完成') : successLabel('已完成');
			}},
			{ label: '编号', name: 'taskNo', index: "task_no", width: 20,hidden:true},
			{ label: '任务名称', name: 'taskName', index: "task_name", width: 40,sortable:false},
			{ label: '客户', name: 'customerName',index: "customer_name",  width: 60,sortable:false,formatter: function(value, options, row){
				return "<div class='table-cell'>" + value + "</div>";
			}},
			{ label: '任务大类', name: 'taskType', index: "task_type", width: 20,formatter: function(value, options, row){
					return primaryLabel(zdb.format('taskType',value,options,row));
			},sortable:false},
			{ label: '明细', name: 'taskClass', index: "task_class", width: 30,formatter: function(value, options, row){
				return primaryLabel(zdb.format('taskClass',value,options,row));
			},sortable:false},
			{ label: '子分类', name: 'taskSubClass', index: "task_sub_class", width: 30,formatter: function(value, options, row){
				return primaryLabel(zdb.format('taskSubClass',value,options,row));
			},sortable:false,hidden:true},
			{ label: '进度流程管理', name: 'progress', width: 30,sortable:false,hidden:true},
			{ label: '办理人员', name: 'employeeName', width: 30,sortable:false},
			{ label: '首次录入日期', name: 'createTime', index: "create_time", width: 40,sortable:true}
        ],
		viewrecords: true,
		width:	'100%',
        height: '100%',
        rowNum: 20,
		rowList : [20,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
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
        subGrid : true,
        subGridRowExpanded: function(subgrid_id, row_id) {
    		var subgrid_table_id, pager_id;
    		subgrid_table_id = subgrid_id+"_t";
    		pager_id = "p_"+subgrid_table_id;
    		var taskId = $("#jqGrid").getRowData(row_id).id;
    		$("#"+subgrid_id).html("<table id='"+subgrid_table_id+"' class='scroll'></table><div id='"+pager_id+"' class='scroll'></div>");
    		$("#"+subgrid_table_id).jqGrid({
    			url:baseURL + "cal/task/item/list?taskId=" + taskId,
    			datatype: "json",
    			colNames: ['ID','实际状态','状态','具体事项','办理时间','地点','人员'],
    			colModel: [
    				{name:"id",index:"id",key:true, width:30,sortable:false, editable:true,hidden: true},
    				{name: "status", width:20, editoptions:{defaultValue:"0"}, hidden: true },
    				{name: 'statusStr', width: 50, formatter: function(value, options, row){
	            		return switchButton(value,options,row);
	    			}},
    				{
    					name:"taskItemTitle",
    					index:"task_item_title",width:100,sortable:false, editable:true,
    					editrules:{required:true}
    				},
    				{
    					name:"startTime",index:"start_time",
    					width:50,sortable:false, editable:true,
    					//formatter: 'date',
    					edittype: 'text', 
    					editoptions:{
    						size: 13, 
				            maxlengh: 13, 
				            dataInit: function (element) { 
				                $(element).datetimepicker({ 
				                	language:  'zh-CN',
				                	minView:1,
				                	autoclose: 1,
				                	weekStart: 1,
				                	format : 'yyyy-mm-dd hh:00'
				                });
				            }
    					}
    				},
    				{
    					name:"place",index:"place",width:50,sortable:false, editable:true,
    					editrules:{required:true}
    				},
    				{
    					name:"employeeName",index:"employee_name",
    					width:30,align:"left",sortable:false, editable:true,
    					editrules:{required:true},edittype: 'text',
    					editoptions: {
    				        dataInit: function (elem) {
    				            $(elem).autocomplete({
    				                source: baseURL + 'sys/user/nameList',
    				                select: function (event, ui) {
    				                    $("#" + taskId + "0_employeeName").val(ui.item.value);
    				                }
    				            });
    				        }
    					}
    				}
    			],
    		   	rowNum:10,
    		   	pager: pager_id,
    		   	//sortname: 'num',
    		    //sortorder: "asc",
    		   	autowidth:true,
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
    	        editurl: baseURL + "cal/task/item/save",
    		    height: '100%',
    		    gridComplete:function(){
    	        	//隐藏grid底部滚动条
    	        	$("#"+subgrid_table_id).closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
    	        	
    	        	//处理switch button事件
    	        	var switchChange = function(event,state){
    	        		var subGrid = $("#"+subgrid_table_id);
    	        		var ids = subGrid.jqGrid('getDataIDs');
    	        		var finishedItem = 0;
    	        		//统计有多少事项已经完成
    	        		for(var i = 0; i < ids.length; i++){
    	        			status_1 = subGrid.getRowData(ids[i]).status;
    	        			//console.log("status_1=" + status_1);
    	        			if(status_1 == 1){
    	        				finishedItem++;
    	        			}
    	        		};
    	        		var result = false;
    	        		var switch_id = this.id;
    	        		var taskItemId = switch_id.substr("switch_".length);
    	        		//console.log("state=" + state);
    	        		var status = state ? 0 : 1;
    	        		var changeStatus = function(){
    	        			$.ajax({
	    	        			async: false,
	    						type: "POST",
	    					    url: baseURL + "cal/task/updateItemStatus",
	    	                    //contentType: "application/json",
	    					    data: {id:taskItemId,status:status},
	    					    success: function(r){
	    							if(r.code == 0){
	    								result = true;
	    								//如果需要联动修改任务状态
	    								if(r.taskStatus != null && r.taskStatus != undefined){
	    									$("#jqGrid").setCell(taskId,"status",r.taskStatus);
	    									subGrid.setCell(taskItemId,"status", status);
	    								}
	    							}else{
	    								alert(r.msg);
	    								result = false;
	    							}
	    						}
	    					});
    	        		}
    	        		var cancelchangeStatus = function(){
    	        			//$(this).bootstrapSwitch('setState',false);
    	        			console.log("取消了完成任务事项");
    	        		}
    	        		//如果是完成事项,并且是最后一个,弹出确认框
//    	        		if(finishedItem >= (ids.length-1) && status == 1){
//    	        			confirm("完成所有事项,任务也会变为完成状态,确定要完成吗?", changeStatus,cancelchangeStatus);
//    	        		}else {
    	        			changeStatus();
//    	        		}
    	        		console.log("result=" + result);
    	        		return result;
    	    		}
    	        	$(".my-switch").bootstrapSwitch({
    	        		onSwitchChange: switchChange
    	        	});
    	        }
    		});
    		$("#"+subgrid_table_id).jqGrid('navGrid',"#"+pager_id,
    				{
    					edit:false,add:false,del:true,
    					delfunc:function(id){
    						$.ajax({
	    	        			async: false,
	    						type: "POST",
	    					    url: baseURL + "cal/task/item/delete",
	    	                    //contentType: "application/json",
	    					    data: {id:id},
	    					    success: function(r){
	    							if(r.code == 0){
	    								$("#"+subgrid_table_id).trigger("reloadGrid");
	    							}else{
	    								alert(r.msg);
	    							}
	    						}
	    					});
			    		}
    				},
    				{}, {}, {},{
    		            caption : "查找",
    		            Find : "开始查找",
    		            closeAfterSearch : true
    		        });
    		var editId = null;
    		var editparameters = {
    				keys: true,
                    oneditfunc: function (id) { 
                    	console.log('oneditfunc,id=' + id + ',taskId=' + taskId);
                    	editId = id;
                    },
                    onEnterfunc: function () { console.log('onEnterfunc');},
                    successfunc: function (r) { 
                    	return reloadGridFunc(r);
                    },
                    url: baseURL + "cal/task/item/update?updateTaskId=false",
                    extraparam: {
                    	taskId: function () {
                    		return taskId;
                    	}
                    },
                    aftersavefunc: function (id) { console.log('aftersavefunc'); },
                    errorfunc: function (id) { console.log('errorfunc'); },
                    afterrestorefunc: function (id) { console.log('afterrestorefunc'); },
                    restoreAfterError: function (id) { console.log('restoreAfterError'); },
                    mtype: "POST"
    		};
    		var reloadGridFunc = function (response) {
    			console.log("successfunc: " + response);
    			var result = eval('(' + response.responseText + ')');
    			//保存成功
	            if (result.code == 0) {
	            	var $self = $(this);
	            	setTimeout(function () {
	            		$self.setGridParam({ datatype: 'json' });
	            		$self.trigger("reloadGrid");
	            	}, 500);
	            	return true;
	            }else {
	            	alert(result.msg);
	            	return false;
	            }
    		};
    		var addParams = {
    			rowID : taskId+"0",
    			useFormatter : false,
                useDefValues: true,
                addRowParams: {
                    // here are editOption used for Add
                    keys: true,
                    successfunc: reloadGridFunc,
                    aftersavefunc: function(rowid,response){
                    	console.log("aftersavefunc: " + rowid + ", " + response);
                    },
                    errorfunc: function(rowid,response){
                    	console.log("errorfunc: " + rowid + ", " + response);
                    },
                    afterrestorefunc : function(rowid,response){
                    	console.log("afterrestorefunc: " + rowid + ", " + response);
                    },
                    oneditfunc: function(r){
                    	console.log("oneditfunc: " + r);
                    	
                    },
                    extraparam: {
                        taskId: function () {
                            return taskId;
                        }
                    }
                }                        
            }
    		jQuery("#"+subgrid_table_id).jqGrid('inlineNav',"#"+pager_id,{addParams:addParams,editParams:editparameters});
    	},
    	//双击任务行时,自动展开它的子表格(任务下的事项)
    	ondblClickRow: function (rowid, iRow, iCol, e) {
    		$("#jqGrid").jqGrid("toggleSubGridRow",rowid);
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
    
    //如果是已完成菜单,隐藏新增、修改、删除按钮
    if(status == 1){
    	$("#addBtn").hide();
    	$("#updateBtn").hide();
    	$("#deleteBtn").hide();
    }
});

//自动联想输入客户信息
function customerAutocomplete(){
    $("#customerName").autocomplete({
    	source: baseURL + 'customer/namelist',
    	focus: function( event, ui ) {
            $( "#customerName" ).val( ui.item.value );
            return false;
          },
          select: function( event, ui ) {
            $( "#customerName" ).val( ui.item.value );
        	vm.task.customerName = ui.item.value;
            return false;
          }
    });
}

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			taskName: null
		},
		showList: true,
		title:null,
		taskTypeList:{},
		taskClassList:{},
		taskSubClassList:{},
		task:{},
		userList:{}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增任务";
			vm.taskTypeList = {};
			vm.taskClassList = {};
			vm.taskSubClassList = {};
			vm.task = {};
			vm.task.taskType=1;
			vm.userList = {};
			
			this.getTaskTypeList();
			this.getTaskClassList(1);
			this.getUserList();
			customerAutocomplete();
		},
		update: function () {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改任务";
            this.getTaskTypeList();
			vm.getTask(id);
			//this.getTaskClassList(vm.task.taskType);
			//this.getUserList();
			customerAutocomplete();
		},
		del: function () {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "cal/task/delete",
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
		top: function(){
			var rowid = getSelectedRow();
			if(rowid == null){
				return ;
			}
			$.post(baseURL + "cal/task/top",{id:rowid},function(r){
				if(r.code == 0){
					vm.reload();
				}
			});
		},
		saveOrUpdate: function () {
            if(vm.validator()){
                return ;
            }
			var url = vm.task.id == null ? "cal/task/save" : "cal/task/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.task),
			    success: function(r){
			    	if(r.code === 0){
						alert('操作成功', function(){
							//如果是从新建日程跳转过来的,直接关闭
							if(jumpToAdd == 1){
								parent.closeSelectTab();
							}else {
								vm.reload();
							}
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		getTask: function(id){
			$.get(baseURL + "cal/task/getById/"+id, function(r){
				vm.task = r.task;
				vm.userList = r.userList;
				vm.getTaskClassList(vm.task.taskType);
				if(!isBlank(vm.task.taskClass)){
					vm.getTaskSubClassList(vm.task.taskClass);
				}
			});
		},
		getUserList: function(){
			$.get(baseURL + "sys/user/listAll", function(r){
				vm.userList = r.list;
			});
		},
		getTaskTypeList: function(){
			vm.taskTypeList = zdb.enum2arr('taskType');
		},
		getTaskClassList: function(v){
			vm.taskClassList = zdb.taskClassList(v);
		},
		getTaskSubClassList: function(v){
			vm.taskSubClassList = zdb.taskSubClassList(v);
		},
		reload: function () {
			if(jumpToAdd == 1){
				parent.closeSelectTab();
			}else {
				vm.showList = true;
				var page = $("#jqGrid").jqGrid('getGridParam','page');
				$("#jqGrid").jqGrid('setGridParam',{ 
	                postData:{'taskName': vm.q.taskName},
	                page:page
	            }).trigger("reloadGrid");
			}
		},
		exportTask: function(){
			var params = {'page':1,'limit':10000,'status':status};
			if(vm.q.taskName){
				params.taskName = vm.q.taskName;
			}
			downloadFileByForm(baseURL + "cal/task/export",params);
		},
		exportTaskItem: function(){
			var params = {'page':1,'limit':10000,'status':status};
			downloadFileByForm(baseURL + "cal/task/exportTaskItem",params);
		},
        validator: function () {
            if(isBlank(vm.task.taskName)){
                alert("任务名称不能为空");
                return true;
            }

            if(isBlank(vm.task.taskType)){
                alert("任务类型不能为空");
                return true;
            }

            /*if(isBlank(vm.task.taskNo)){
                alert("编号不能为空");
                return true;
            }*/
            if(isBlank(vm.task.customerName)){
            	alert("客户不能为空");
            	return true;
            }
            if(isBlank(vm.task.employeeId)){
            	alert("办理员工不能为空");
            	return true;
            }

        }
	}
});
var jumpToAdd = T.p("jumpToAdd") || '0';
if(jumpToAdd && jumpToAdd == 1){
	vm.add();
}