function updateMultiplication() {
    $.ajax({
        url: "/multiplications/random"
    }).then(function(data){
        $("#attempt-form").find("input[name='result-attempt']").val("");
        $("#attempt-form").find("input[name='user-alias']").val("");
        $(".multiplication-a").empty().append(data.factorA);
        $(".multiplication-b").empty().append(data.factorB);
    })
}

function updateStats(alias) {
    $.ajax({
        url:"/results?alias=" + alias
    }).then(function(data) {
        $('#stats-body').empty();
        data.forEach(function(row){
            $('#stats-body').append('<tr><td>' + row.id + '</td>'
                + '<td>' + row.multiplication.factorA + ' x ' + row.multiplication.factorB + '</td>'
                + '<td>' + row.resultAttempt + '</td>'
                + '<td>' + (row.correct === true ? 'Yes' : 'No' + '</td></tr>'))
        })
    })
}

$(document).ready(function() {
    updateMultiplication();
    $("#attempt-form").submit(function(event) {
        event.preventDefault();

        var a = $('.multiplication-a').text(),
            b = $('.multiplication-b').text(),
            $form = $(this),
            attempt = $form.find("input[name='result-attempt']").val(),
            userAlias = $form.find("input[name='user-alias']").val();

        var data = {
            user: {alias: userAlias},
            multiplication: {
                factorA: a,
                factorB: b
            },
            resultAttempt: attempt
        };
        $.ajax({
            url: '/results',
            type: 'POST',
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType:"json",
            success: function(result) {
                if(result.correct) {
                    $('.result-message').empty().append("The result is correct! Congratulations!")
                } else {
                    $('.result-message').empty().append("Oops that's not correct! But keep trying!")
                }
                updateStats(userAlias);
            }
        });
        updateMultiplication();
    });
});