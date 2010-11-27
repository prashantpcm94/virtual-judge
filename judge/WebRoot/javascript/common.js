// ==============================================================
// 方法名: comfirmDeleteProblem
// 方法描述: 本方法主要实现确认Problem的删除
// 返回值：
// false: 不提交请求
// ==============================================================
function comfirmDeleteProblem(id){
	if (confirm("Sure to delete this problem?")){
		location = 'problem/deleteProblem.action?id=' + id;
	}
}

//==============================================================
//方法名: comfirmDeleteContest
//方法描述: 本方法主要实现确认Contest的删除
//返回值：
//false: 不提交请求
//==============================================================
function comfirmDeleteContest(id){
	if (confirm("Sure to delete this contest?")){
		location = 'contest/deleteContest.action?cid=' + id;
	}
}
