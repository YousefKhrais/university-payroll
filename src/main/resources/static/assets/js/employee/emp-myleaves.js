$((function (e) {
    var a = {
        series: [14, 8, 20, 18],
        chart: {height: 330, type: "donut"},
        dataLabels: {enabled: !1},
        legend: {show: !1},
        stroke: {show: !0, width: 0},
        plotOptions: {
            pie: {
                donut: {
                    size: "85%",
                    background: "transparent",
                    labels: {
                        show: !0,
                        name: {show: !0, fontSize: "29px", color: "#6c6f9a", offsetY: -10},
                        value: {show: !0, fontSize: "26px", color: void 0, offsetY: 16},
                        total: {
                            show: !0,
                            showAlways: !1,
                            label: "Total Leaves",
                            fontSize: "22px",
                            fontWeight: 600,
                            color: "#373d3f"
                        }
                    }
                }
            }
        },
        responsive: [{breakpoint: 480, options: {legend: {show: !1}}}],
        labels: ["Casual Leaves", "Sick Leaves", "Gifted Leaves", "Remaining Leaves"],
        colors: ["#3366ff", "#f7284a", "#fe7f00", "#01c353"]
    };
    new ApexCharts(document.querySelector("#leavesoverview"), a).render(), $("#emp-attendance").DataTable({
        order: [],
        columnDefs: [{orderable: !1, targets: [0, 5]}],
        language: {searchPlaceholder: "Search...", sSearch: ""}
    }), $('input[name="singledaterange"]').daterangepicker({singleDatePicker: !0}), $('input[name="daterange"]').daterangepicker({opens: "left"}, (function (e, a, o) {
        console.log("A new date selection was made: " + e.format("MMMM D, YYYY") + " to " + a.format("MMMM D, YYYY"))
    })), $("#daterange-categories").on("change", (function () {
        $(".leave-content").hide(), $("#" + $(this).val()).show()
    })), $(".select2").select2({minimumResultsForSearch: 1 / 0, width: "100%"})
}));