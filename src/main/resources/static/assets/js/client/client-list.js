(() => {
    function e(e, r, a) {
        return r in e ? Object.defineProperty(e, r, {
            value: a,
            enumerable: !0,
            configurable: !0,
            writable: !0
        }) : e[r] = a, e
    }

    !function () {
        "use strict";
        var r;
        $("#client-list").DataTable((e(r = {order: [[0, "desc"]]}, "order", []), e(r, "columnDefs", [{
            orderable: !1,
            targets: [5]
        }]), e(r, "language", {
            searchPlaceholder: "Search...",
            sSearch: ""
        }), r)), $(".fc-datepicker").datepicker({
            dateFormat: "dd M yy",
            monthNamesShort: ["Jan", "Feb", "Mar", "Apr", "Maj", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"],
            zIndex: 999998
        }), $(".summernote").summernote({placeholder: "", tabsize: 1, height: 120})
    }(jQuery)
})();