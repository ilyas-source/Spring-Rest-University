function makeActiveNav (document, navIndex) {
    document.getElementsByTagName("nav")[0]
        .getElementsByTagName("li")[navIndex]
        .classList.add("active");
}
