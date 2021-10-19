document.addEventListener('DOMContentLoaded', function() {
    var calendarEl = document.getElementById('calendar');
    var calendar = new FullCalendar.Calendar(calendarEl, {
        events: function (info, callback) {
            $.get(
                '/university/lectures/schedule/calendar',
                function (calendar) {
                    let events = [];
                    calendar.forEach(function (lecture) {
                        let date=lecture.date;
                        let time=lecture.timeslot.beginTime;
                        let start=date[0]+'-';
                        if(date[1]<10) {
                            start+="0";
                        }
                        start+=date[1];
                        if(date[1]<10) {
                            start+="0";
                        }
                        start+='-';
                        if(date[2]<10) {
                            start+="0";
                        }
                        start+=date[2]+"T";
                        if(time[0]<10) {
                            start+="0";
                        }
                        start+=time[0]+":";
                        if(time[1]<10) {
                            start+="0";
                        }
                        start+=time[1]+":00";
                        events.push({
                            id: lecture.id,
                            title: lecture.subject.name,
                            start: start,
                        });
                    });
                    callback(events);
                });
        },
        initialView: 'timeGridDay'
    });
    let period = document.getElementById('period').getAttribute('value');
    console.log(period);
    if(period=='month') {
        console.log('Changing view to month');
        calendar.changeView('dayGridMonth');
    }
    calendar.render();
});