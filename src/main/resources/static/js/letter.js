$(function(){
	$("#sendBtn").click(send_letter);
	$(".close").click(delete_msg);
});

function send_letter() {
	$("#sendModal").modal("hide");
	var toName = $("#recipient-name").val();
	var content = $("#message-text").val();

	// 异步请求
	$.post(
		CONTEXT_PATH + "/letter/send",
		{toName: toName, content: content},
		function (data) {
			data = $.parseJSON(data);
			// 从提示框中返回消息
			if (data.code == 0) {
				$("#hintBody").text("发送成功！");
			} else {
				$("#hintBody").text(data.msg);
			}
			$("#hintModal").modal("show");
			setTimeout(function(){
				$("#hintModal").modal("hide");
				// 隐藏后刷新页面
				if(data.code == 0) {
					window.location.reload();
				}
			}, 2000);
		}
	)



}

function delete_msg() {
	// TODO 删除数据
	$(this).parents(".media").remove();
}