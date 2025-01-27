document.getElementById("apiForm").addEventListener("submit", function (e) {
    e.preventDefault();

    const name = document.getElementById("name").value;
    fetch(`/app/hello?name=${encodeURIComponent(name)}`)
        .then((response) => response.json())
        .then((data) => {
            document.getElementById("response").innerHTML = `<p>${data.message}</p>`;
        })
        .catch((error) => {
            document.getElementById("response").innerHTML = `<p>Error: ${error.message}</p>`;
        });
});
