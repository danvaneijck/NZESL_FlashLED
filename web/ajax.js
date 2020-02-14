$(document).ready(function() {
    $('#button1').click(function() {
        var ID = $('#item_ID').val();
        var button = $('#button1').val();
        $.get('Main', {
            item_ID : ID,
            button: button
        }, function(responseText) {
            $('#ajaxResponse').text(responseText);
        });
    });
    $('#button2').click(function() {
        var ID = $('#item_ID').val();
        var button = $('#button2').val();
        $.get('Main', {
            item_ID : ID,
            button: button
        }, function(responseText) {
            $('#ajaxResponse').text(responseText);
        });
    });
});
