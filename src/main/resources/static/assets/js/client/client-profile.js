(() => {
    function e(e, a, r) {
        return a in e ? Object.defineProperty(e, a, {
            value: r,
            enumerable: !0,
            configurable: !0,
            writable: !0
        }) : e[a] = r, e
    }

    $((function (a) {
        var r, o, l, c, s;
        $("#task-list").DataTable((e(r = {order: [[0, "desc"]]}, "order", []), e(r, "columnDefs", [{
            orderable: !1,
            targets: [7]
        }]), e(r, "language", {
            searchPlaceholder: "Search...",
            sSearch: ""
        }), r)), $("#project-list").DataTable((e(o = {order: [[0, "desc"]]}, "order", []), e(o, "columnDefs", [{
            orderable: !1,
            targets: [7]
        }]), e(o, "language", {
            searchPlaceholder: "Search...",
            sSearch: ""
        }), o)), $("#payment-tables").DataTable((e(l = {order: [[0, "desc"]]}, "order", []), e(l, "columnDefs", [{
            orderable: !1,
            targets: [5]
        }]), e(l, "language", {
            searchPlaceholder: "Search...",
            sSearch: ""
        }), l)), $("#invoice-tables").DataTable((e(c = {order: [[0, "desc"]]}, "order", []), e(c, "columnDefs", [{
            orderable: !1,
            targets: [6]
        }]), e(c, "language", {
            searchPlaceholder: "Search...",
            sSearch: ""
        }), c)), $("#ticket-tables").DataTable((e(s = {order: [[0, "desc"]]}, "order", []), e(s, "columnDefs", [{
            orderable: !1,
            targets: [5]
        }]), e(s, "language", {
            searchPlaceholder: "Search...",
            sSearch: ""
        }), s)), $(".fc-datepicker").datepicker({
            dateFormat: "dd M yy",
            monthNamesShort: ["Jan", "Feb", "Mar", "Apr", "Maj", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"],
            zIndex: 999998
        }), $(".summernote").summernote({
            placeholder: "",
            tabsize: 1,
            height: 200
        }), $(".select2").select2({
            minimumResultsForSearch: 1 / 0,
            width: "100%"
        }), $(document).on("ready", (function () {
            $(".dismiss").on("click", (function () {
                $(".sidebar-modal").removeClass("active"), $("body").removeClass("overlay-open")
            })), $(".sidebarmodal-collpase").on("click", (function () {
                $(".sidebar-modal").addClass("active"), $("body").addClass("overlay-open")
            })), $("body").append('<div class="overlay"></div>'), $(".overlay").on("click touchstart", (function () {
                $("body").removeClass("overlay-open"), $(".sidebar-modal").removeClass("active")
            }))
        }))
    }))
})();