$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	$("#publishModal").modal("hide");
	// 获取标题和内容
	var title = $("#recipient-name").val();
	var content = $("#message-text").val();
	$.post(
		CONTEXT_PATH + "/discuss/add",
		{"title":title, "content":content},
		function (data) {
			data = $.parseJSON(data);
			// 提示框中返回消息
			$("#hintBody").text(data.msg);
			$("#hintModal").modal("show");
			// 2s后，自动隐藏提示框
			setTimeout(function(){
				$("#hintModal").modal("hide");
				// 隐藏后刷新页面
				if(data.code == 0) {
					window.location.reload();
				}
			}, 2000);
		}
	);
}