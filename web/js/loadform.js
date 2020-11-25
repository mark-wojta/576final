function onSelectReportType(ele){
    var form = $(ele).parent().parent();
    var select = $(form).find(".mag_select");

    switch (ele.value) {
        case "is_tornado":
            select.find('option').remove();
            select.append($("<option></option>")
                .attr("value", "")
                .text("Magnitude"));
            selectValues = ['0', '1', '2', '3', '4',
                '5'];
            $.each(selectValues, function (index, value) {
                select.append($("<option></option>")
                    .attr("value", value)
                    .text(value));
            });
            break;
        case "is_hail":
            select.find('option').remove();
            select.append($("<option></option>")
                .attr("value","")
                .text("Magnitude"));
            selectValues = ['0-1', '1-2', '2-3', '3-4', '4-5',
                '5-6'];
            $.each(selectValues, function(index,value) {
                select.append($("<option></option>")
                    .attr("value",value)
                    .text(value));
            });
            break;
        case "is_wind":
            select.find('option').remove();
            select.append($("<option></option>")
                .attr("value","")
                .text("Magnitude"));
            selectValues = ['0-25 mph', '25-50 mph', '50-75 mph', '75-100 mph',
                '> 100 mph'];
            $.each(selectValues, function(index,value) {
                select.append($("<option></option>")
                    .attr("value",value)
                    .text(value));
            });
            break;
        default:
            $(form).find(".mag").css("visibility", "visible");
            return;
    }
    $(form).find(".additional_msg_div").css("visibility", "visible");
}

function createReport(event) {
    event.preventDefault(); // stop form from submitting normally

    var a = $("#create_report_form").serializeArray();
    var timestamp = new Date().getTime();
    a.push({ name: "tab_id", value: "0" }, { name: "longitude", value: place.geometry.location.lng() },
        { name: "latitude", value: place.geometry.location.lat() }, { name: "time_stamp", value: timestamp });

    $.ajax({
        url: 'HttpServlet',
        type: 'POST',
        data: a,
        success: function() {
            alert("The report is successfully submitted!");
            //when report submitted successfully, user clicks ok and map generates with new report added to all reports
            //(answer to question 4 part 5 in lab 6)
            $.ajax({
                url: 'HttpServlet',
                type: 'POST',
                data: { "tab_id": "1"},
                success: function(reports) {
                    mapInitialization(reports);
                    //re-centers map to place where report is created
                    //(answer to question 4 part 6, bonus, in lab 6)
                    onPlaceChanged();
                },
                error: function(xhr, status, error) {
                    alert("An AJAX error occured: " + status + "\nError: " + error);
                }
            });
        },
        error: function(xhr, status, error) {
            alert("Status: " + status + "\nError: " + error);
        },

    });

    $(".additional_msg_div").css("visibility", "hidden");
    document.getElementById("create_report_form").reset();


}

function queryReport(event) {
    event.preventDefault(); // stop form from submitting normally

    var a = $("#query_report_form").serializeArray();
    a.push({ name: "tab_id", value: "1" });
    a = a.filter(function(item){return item.value != '';});
    $.ajax({
        url: 'HttpServlet',
        type: 'POST',
        data: a,
        success: function(reports) {
            mapInitialization(reports);
        },
        error: function(xhr, status, error) {
            alert("Status: " + status + "\nError: " + error);
        }
    });
}

function dateSlider(){
    $( function() {
        $( "#slider-range" ).slider({
            range: true,
            min: 0,
            max: 500,
            values: [ 75, 300 ],
            slide: function( event, ui ) {
                $( "#amount" ).val( "$" + ui.values[ 0 ] + " - $" + ui.values[ 1 ] );
            }
        });
        $( "#amount" ).val( "$" + $( "#slider-range" ).slider( "values", 0 ) +
            " - $" + $( "#slider-range" ).slider( "values", 1 ) );
    } );
}

$("#create_report_form").on("submit",createReport);
$("#query_report_form").on("submit",queryReport);