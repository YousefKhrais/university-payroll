(() => {
    function e(e, r, a) {
        return r in e ? Object.defineProperty(e, r, {
            value: a,
            enumerable: !0,
            configurable: !0,
            writable: !0
        }) : e[r] = a, e
    }

    $((function (r) {
        var a;
        $("#emp-pay").DataTable((e(a = {order: [[0, "desc"]]}, "order", []), e(a, "columnDefs", [{
            orderable: !1,
            targets: [0, 5]
        }]), e(a, "language", {
            searchPlaceholder: "Search...",
            sSearch: ""
        }), a)), $(".select2").select2({minimumResultsForSearch: 1 / 0, width: "100%"})
    }))
})();