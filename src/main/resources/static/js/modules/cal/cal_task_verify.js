
$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'cal/task/verify_list',
        datatype: "json",
        colModel: [	
        	{ label: 'ID', name: 'id', width: 10, key: true, hidden: false },
        	{ label: '任务事项名称', name: 'taskItemTitle', index: "task_item_title", width: 40,sortable:false},
        	{ label: 'task_item_id', name: 'taskItemId', width: 10, hidden: true },
        	{ label: '所属任务', name: 'taskName', index: "task_name", width: 40,sortable:false},
        	{ label: 'verifyStatus', name: 'verifyStatus', width: 10, hidden: true },
        	{ label: '审核状态', name: 'verifyStatusStr', width: 20, formatter: function(v, o, r){
				return r.verifyStatus == 0 ? '未审核' : (r.verifyStatus == 1 ? '审核通过' : '审核不通过');
			}},
			{ label: '申请人', name: 'username', index: "username", width: 20},
			{ label: '申请时间', name: 'createTime', width: 30,sortable:false},
			{ label: '审核人', name: 'username1',index: "username1",  width: 20,sortable:false},
			{ label: '审核时间', name: 'updateTime', index: "update_time", width: 40,sortable:false,formatter: function(v, o, r){
				return r.verifyStatus == 1 ? v : '';
			}}
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
			taskItemTitle: null,
			verifyStatus:null
		},
		verifyStatusEnum:[{'label':'未审核','value':0},{'label':'审核通过','value':1},{'label':'审核不通过','value':2}],
		showList:true
	},
	methods: {
		query: function () {
			vm.reload();
		},
		verifyStatus: function (verifyStatus) {
			var verifyId = getSelectedRow();
			if(verifyId == null){
				return ;
			}
			//console.log("verifyId=" + verifyId);
			var row = $("#jqGrid").getRowData(verifyId);
			var rd = {id:row.id,verifyStatus:verifyStatus,taskItemId:row.taskItemId};
			confirm('确定要审批选中的记录?', function(){
				var url = "cal/task/item/verify";
				$.ajax({
					type: "POST",
					url: baseURL + url,
					contentType: "application/json",
					data: JSON.stringify(rd),
					success: function(r){
						if(r.code === 0){
							alert('操作成功');
							vm.reload();
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		verify: function(){
			this.verifyStatus(1);
		},
		//撤销审批
		unverify: function(){
			this.verifyStatus(0);
		},
		reload: function () {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                postData:{'taskItemTitle': vm.q.taskItemTitle,'verifyStatus':vm.q.verifyStatus},
                page:page
            }).trigger("reloadGrid");
		}
	}
});
