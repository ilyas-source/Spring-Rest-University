function sortTableByColumn(table, column, asc = true) {
    const dirModifier = asc ? 1 : -1;
    const tBody = table.tBodies[0];
    const rows = Array.from(tBody.querySelectorAll("tr"));

    const sortedRows = rows.sort((a, b) => {
        const aColText = a.querySelector(`td:nth-child(${column + 1})`).textContent.trim();
        const bColText = b.querySelector(`td:nth-child(${column + 1})`).textContent.trim();

        return aColText > bColText ? (1 * dirModifier) : (-1 * dirModifier);
    });

    while (tBody.firstChild) {
        tBody.removeChild(tBody.firstChild);
    }

    tBody.append(...sortedRows);

    table.querySelectorAll("th").forEach(th => th.classList.remove("th-sort-asc", "th-sort-desc"));
    let header = table.querySelector(`th:nth-child(${column + 1})`);
    header.classList.toggle("th-sort-asc", asc);
    header.classList.toggle("th-sort-desc", !asc);
}

function updateNavButtons(page, size, sort) {
    let firstButton = document.getElementById('first-button');
    let prevButton = document.getElementById('prev-button');
    let nextButton = document.getElementById('next-button');

    prevButton.parentElement.classList.add('disabled');
    if(hasPreviousPage) {
        prevButton.parentElement.classList.remove('disabled');
    };

    firstButton.setAttribute('href','?page=0&size=' + size + '&sort=' + sort);
    prevButton.setAttribute('href','?page=' + (page - 1) + '&size=' + size + '&sort=' + sort);
    nextButton.setAttribute('href','?page=' + (page + 1) + '&size=' + size + '&sort=' + sort);
}

document.querySelectorAll(".table-sortable th").forEach(headerCell => {
    headerCell.addEventListener("click", () => {
        const tableElement = headerCell.parentElement.parentElement.parentElement;
        const headerIndex = Array.prototype.indexOf.call(headerCell.parentElement.children, headerCell);
        const currentIsAscending = headerCell.classList.contains("th-sort-asc");

        sortTableByColumn(tableElement, headerIndex, !currentIsAscending);
        sorting = headerCell.getAttribute('id');
        updateNavButtons(pageNumber, pageSize, sorting);
        document.getElementById('first-button').click();
    });
});

updateNavButtons(pageNumber, pageSize, sorting);