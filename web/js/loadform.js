function onSelectReportType(ele){
    var form = $(ele).parent().parent();
    var select = $(form).find(".mag_select");
    var length_select = $(form).find(".length_select");
    var len_label = $(form).find(".length");
    var fatal_select = $(form).find(".fatal_select");
    var fatal_label = $(form).find(".fatalities");
    var inj_select = $(form).find(".inj_select");

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
            len_label.text("Length:");
            length_select.find('option').remove();
            length_select.append($("<option></option>")
                .attr("value", "")
                .text("Length"));
            lenselectValues = ['0-25 miles', '25-50 miles', '50-75 miles', '75-100 miles', '> 100 miles'];
            $.each(lenselectValues, function (index, value) {
                length_select.append($("<option></option>")
                    .attr("value", value)
                    .text(value));
            });
            fatal_label.text("Fatalities:");
            fatal_select.find('option').remove();
            fatal_select.append($("<option></option>")
                .attr("value", "")
                .text("Fatalities"));
            fatalselectValues = ['0', '1', '2', '3', '4',
                '5', '> 5'];
            $.each(fatalselectValues, function (index, value) {
                fatal_select.append($("<option></option>")
                    .attr("value", value)
                    .text(value));
            });
            inj_select.find('option').remove();
            inj_select.append($("<option></option>")
                .attr("value", "")
                .text("Injuries"));
            injselectValues = ['0-5', '6-10', '11-20', '21-50', '> 51'];
            $.each(injselectValues, function (index, value) {
                inj_select.append($("<option></option>")
                    .attr("value", value)
                    .text(value));
            });
            $(form).find(".fatal_div").css("visibility", "visible");
            $(form).find(".length_div").css("visibility", "visible");
            break;
        case "is_hail":
            $(form).find(".fatal_div").css("visibility", "visible");
            select.find('option').remove();
            select.append($("<option></option>")
                .attr("value","")
                .text("Magnitude"));
            selectValues = ['0-1', '1-2', '2-3', '3-4', '4-5',
                '5-6', '6-7'];
            $.each(selectValues, function(index,value) {
                select.append($("<option></option>")
                    .attr("value",value)
                    .text(value));
            });
            inj_select.find('option').remove();
            inj_select.append($("<option></option>")
                .attr("value", "")
                .text("Injuries"));
            injselectValues = ['0', '1', '2', '3', '4', '> 4'];
            $.each(injselectValues, function (index, value) {
                inj_select.append($("<option></option>")
                    .attr("value", value)
                    .text(value));
            });
            $(form).find(".fatal_div").css("visibility", "hidden");
            $(form).find(".length_div").css("visibility", "hidden");
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
            fatal_label.text("Fatalities:");
            fatal_select.find('option').remove();
            fatal_select.append($("<option></option>")
                .attr("value", "")
                .text("Fatalities"));
            fatalselectValues = ['0', '1', '2'];
            $.each(fatalselectValues, function (index, value) {
                fatal_select.append($("<option></option>")
                    .attr("value", value)
                    .text(value));
            });
            inj_select.find('option').remove();
            inj_select.append($("<option></option>")
                .attr("value", "")
                .text("Injuries"));
            injselectValues = ['0', '1', '2', '4', '5', '>5'];
            $.each(injselectValues, function (index, value) {
                inj_select.append($("<option></option>")
                    .attr("value", value)
                    .text(value));
            });
            $(form).find(".fatal_div").css("visibility", "visible");
            $(form).find(".length_div").css("visibility", "hidden");
            break;
        default:
            $(form).find(".fatal_div").css("visibility", "hidden");
            $(form).find(".length_div").css("visibility", "hidden");
            return;
    }
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
                    alert("An AJAX error occurred: " + status + "\nError: " + error);
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
            min: 1950,
            max: 2018,
            values: [ 1950, 2018 ],
            slide: function( event, ui ) {
                $( "#date" ).val( ui.values[ 0 ] + "-" + ui.values[ 1 ] );
            }
        });
        $( "#date" ).val($( "#slider-range" ).slider( "values", 0 ) +
            "-" + $( "#slider-range" ).slider( "values", 1 ) );
    } );
    $( function() {
        $( "#slider-range-month" ).slider({
            range: true,
            min: 1,
            max: 12,
            values: [ 1, 12 ],
            slide: function( event, ui ) {
                $( "#month" ).val( ui.values[ 0 ] + "-" + ui.values[ 1 ] );
            }
        });
        $( "#month" ).val($( "#slider-range-month" ).slider( "values", 0 ) +
            "-" + $( "#slider-range-month" ).slider( "values", 1 ) );
    } );
}


$("#create_report_form").on("submit",createReport);
$("#query_report_form").on("submit",queryReport);
$(window).on("load", dateSlider);