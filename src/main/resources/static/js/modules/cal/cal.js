function formatDate(v){
	return v.format("HH:mm");
}

//格式化FullCalendar的日期
function formatFCDateToHour(v){
//	return new Date(v).format("yyyy-MM-dd HH:00");
	return v.format("YYYY-MM-DD HH:00");
}

//格式化日期并加上当前时间
function formatDayAndCurrentHour(v){
	return v.format("YYYY-MM-DD") + " " + (new Date()).format("hh:00");
}

function updateEventTime(){
	console.log("updateEventTime");
}

var vm = new Vue({
	el:'#rrapp',
	data:{
		isSave:false,
		taskItem:{
			id:0,
			startTime:'',
			taskItemTitle:'',
			employeeName:'',
			place:'',
			status: 0,
			taskId:'',
			taskName:''
		},
		tasks:[],
		userList:[]
	},
	methods: {
		add: function(calEvent){
			//this.start  = formatDayAndCurrentHour(calEvent);
			this.taskItem.id = 0;
			this.taskItem.startTime = formatDayAndCurrentHour(calEvent);;
			this.taskItem.taskItemTitle  = "";
			this.taskItem.employeeName  = "";
			this.taskItem.place  = "";
			this.taskItem.status = 0;
			this.taskItem.taskId = "";
			this.taskItem.taskName = "";
		},
		update: function(calEvent){
			this.taskItem.id = calEvent.id;
			this.taskItem.startTime = formatFCDateToHour(calEvent.start);
			this.taskItem.taskItemTitle = calEvent.taskItemTitle;
			this.taskItem.employeeName = calEvent.employeeName;
			this.taskItem.place = calEvent.place;
			this.taskItem.status = calEvent.status;
			//this.taskItem.taskId = calEvent.taskId;
			this.taskItem.taskName = calEvent.taskName;
		}
	}
});

//var eventTitle;	//新建或者修改日程的标题
//var eventUrl;	//新建或者修改日程的Url
//var evntBtn;	//新建或者修改按钮
var lastClick = [0,0];		//上次点击day or event的时间戳
//var lastClickEvent=0;		//上次点击event的时间戳

//判断是否双击
function isDbClick(jsEvent,i){
	var timeStamp = jsEvent.timeStamp;
	if(lastClick[i] == 0){
		lastClick[i] = timeStamp;
		return false;
	}else {
		var timeInterval = timeStamp - lastClick[i];
		//console.log("click interval: " + timeInterval);
		lastClick[i] = timeStamp;
		if(timeInterval >= 500){
			return false;
		}
	}
	return true;
}

//对新建或修改的数据进行校验
function checkData(data, isSave){
	if(data.start == ''){
		return "请输入任务时间";
	}
	if(data.taskItemTitle == ''){
		return "请输入任务事项";
	}
	if(data.place == ''){
		return "请输入任务地点";
	}
	if(data.employeeName == ''){
		return "请输入办理员工";
	}
	if(isSave && data.taskId == ''){
		return "请选择所属任务";
	}
	return null;
}

//当点击事件或日历时,修改或者新建日程
function saveOrUpdateEvent(calEvent, jsEvent, view) {
	var btns,eventTitle,eventUrl;	//弹出框的按钮,标题,请求后台的url
	if(!vm.isSave){
		vm.update(calEvent);
		btns = ['确定','删除','取消'];
		eventTitle = "修改日程";
		eventUrl = baseURL + "cal/task/item/update";
	}else {
		vm.add(calEvent);
		btns = ['确定','取消'];
		eventTitle = "新建日程";
		eventUrl = baseURL + "cal/task/item/save";
	}
	layer.open({
		type: 1,
		skin: 'layui-layer-molv',
		title: eventTitle,
		area: ['440px', '420px'],
		shadeClose: false,
		zIndex:99,
		content: $("#eventLayer"),
		btn: btns,
		btn1: function (index) {
			var err = checkData(vm.taskItem, vm.isSave);
			if(err != null){
				alert(err);
				return;
			}
			$.ajax({
				type: "POST",
			    url: eventUrl,
			    data: vm.taskItem,
			    dataType: "json",
			    success: function(r){
					if(r.code == 0){
						layer.close(index);
						layer.alert('操作成功' + (r.msg ? '('+r.msg+')': ''), function(index1){
							layer.close(index1);
							refreshEvent();
						});
					}else{
						layer.alert(r.msg);
					}
				}
			});
        },
        btn2: function(index){
        	//按钮>2说明是删除按钮
        	if(this.btn.length > 2){
        		$.post(baseURL + "cal/task/item/delete",{id:vm.taskItem.id},function(r){
        			if(r.code == 0){
        				refreshEvent();
        			}else {
        				alert(r.msg);
        			}
        		});
        	}
        }
	});
}

function createTask(){
	parent.addTab("新建任务",baseURL + "modules/cal/task_list.html?status=0&jumpToAdd=1");
}

var refreshEvent = function(){
	var index = onloading();
	$('#calendar').fullCalendar("refetchEvents");
	removeload(index);
}

$(function() {
	var parent_width = $(parent.document.body).width();
	var parent_height = $(parent.document.body).height();
	//console.log("width="+parent_width + ",height=" + parent_height);
	//根据高宽设置比例
	var aspectRatio = parent_width/parent_height+0.2;
    $('#calendar').fullCalendar({
        // put your options and callbacks here
    	locale: 'zh-cn',
    	header: {
			left: 'prev,next today',
			center: 'title',
			right: 'month,agendaWeek,agendaDay,listWeek'
		},
		navLinks: true, // can click day/week names to navigate views
    	editable: true,
    	aspectRatio:aspectRatio,
    	eventLimit: true, // allow "more" link when too many events
    	eventSources: [
            {
                url: baseURL + "cal/task/event/list/1",//已完成任务
                color: '#ACD6FF',    // an option!
                textColor: 'black'  // an option!
            },
            {
                url: baseURL + "cal/task/event/list/0",
                color: '#FFDCB9',    // an option!
                textColor: 'black'  // an option!
            }
        ],
        eventRender: function(event, element) {
        	var html = [
	        		"<div class='fc-content'>",
		        		"<span class='fc-title'>",
		        			event.place,"&nbsp;&nbsp;",
		        			event.taskItemTitle,"&nbsp;&nbsp;",
		        			event.employeeName,"&nbsp;&nbsp;",
		        		"</span>",
		    		"</div>"
	    	];
        	var elementHtml = [
        		"<div class='fc-content'>",
	        		"<span class='fc-title' style='overflow: hidden;text-overflow: ellipsis; white-space: nowrap;display:block;'>",
	        			formatDate(event.start),
	        			event.place,"&nbsp;&nbsp;",
	        			event.taskItemTitle,"&nbsp;&nbsp;",
	        			event.employeeName,"&nbsp;&nbsp;",
	        		"</span>",
        		"</div>"
        	];
        	element.html(elementHtml.join(""));
        	element.qtip({
                content: {
                	text:html.join(""),
                	title:formatDate(event.start)
                }
            });
        },
        eventDrop: function(event, delta, revertFunc) {
            //alert(event.title + " was dropped on " + event.start.format(),updateEventTime);
        	//console.log(delta);
        	//console.log(delta.days());
        	var uet = function(){
        		$.ajax({
        			type:'post',
            		url:baseURL + "cal/task/item/move",
            		dataType: 'json',
            		data:{
            			id:event.id,
            			days:delta.days()
            		},
            		success: function(res){
            			console.log("success");
            		},
            		error: function(xhr,err,e){
            			revertFunc();
            		}
        		});
        	}
        	var msg = event.status==0 ? '任务已完成,确定要移动吗?' : '确定要移动吗?';
            confirm(msg, uet,revertFunc);
        },
        dayClick: function(date, jsEvent, view) {
        	if(!isDbClick(jsEvent,0)){
        		return;
        	}
        	vm.isSave = true;
			saveOrUpdateEvent(date, jsEvent, view);
        },
        eventClick: function(calEvent, jsEvent, view) {
        	if(!isDbClick(jsEvent,1)){
        		return;
        	}
        	vm.isSave = false;
        	saveOrUpdateEvent(calEvent, jsEvent, view);
        }
    });
    
    //添加自定义按钮
    $('button.fc-today-button').after('<button class="fc-button fc-state-default fc-corner-left fc-corner-right" onclick="refreshEvent();">刷新</button>'); 
    
    //员工输入框绑定自动完成
    $("#employeeName").autocomplete({
        /*source: baseURL + 'sys/user/nameList',
        select: function (event, ui) {
        	vm.taskItem.employeeName = ui.item.value;
        }*/
    	source: function( request, response ) {
            $.getJSON(baseURL + 'sys/user/nameList?status=1', {
              term: extractLast( request.term )
            }, response );
          },
        search: function() {
            // custom minLength
            var term = extractLast( this.value );
            if ( term.length < 1 ) {
              return false;
            }
          },
        select: function (event, ui) {
        	var terms = split(this.value);
        	terms.pop();
        	terms.push( ui.item.value );
        	terms.push( "" );
            this.value = terms.join( "," );
            vm.taskItem.employeeName = this.value;
            return false;
        }
    });
    //任务输入框自动完成
    $("#taskName").autocomplete({
    	minLength: 0,
    	source: baseURL + 'cal/task/taskNamelist',
    	select: function (event, ui) {
    		vm.taskItem.taskName = ui.item.label;
    		vm.taskItem.taskId = ui.item.value;
    		$(this).autocomplete('disable');
    	}
    }).blur(function(){
        $(this).autocomplete('enable');
    }).focus(function () {
        $(this).autocomplete('search', '');
    });
    
    $('#datetimepicker').datetimepicker({
    		language:  'zh-CN',
	    	minView:1,
	    	autoclose: 1,
	    	format : 'yyyy-mm-dd hh:00',
	    	weekStart: 1
    	}
    ).on('hide', function(ev){
    	//var startTime = ev.date.format("yyyy-MM-dd hh:00");
    	//console.log(startTime);
    	//vm.taskItem.startTime = startTime;
    	vm.taskItem.startTime = $(this).val();
    });

});