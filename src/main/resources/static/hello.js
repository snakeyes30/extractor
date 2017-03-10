$(document).ready(function() {
    $.ajax({
        url: "http://localhost:8080/files"
    }).then(function(data, status, jqxhr) {
        console.log(jqxhr);
        var div3Content = '';
        for(var i = 0; i < data.length; i++)
        {
            var file = encodeURIComponent(data[i]);
            var filename = data[i].substring(data[i].lastIndexOf('\\')+1);
            div3Content += '<p><a href="/extract?file=' + file + '">' + filename + '</a></p>'; // if Name is property of your Person object
        }
        $('#list').append(div3Content);
    });
});