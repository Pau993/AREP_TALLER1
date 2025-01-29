document.getElementById("helloForm").addEventListener("submit", async function (event) {
    event.preventDefault();

    const name = document.getElementById("name").value;
    const responseDiv = document.getElementById("response");

    try {
        const response = await fetch(`/app/hello?name=${encodeURIComponent(name)}`);
        const data = await response.json();

        responseDiv.textContent = data.message;
    } catch (error) {
        responseDiv.textContent = "Error al comunicarse con el servidor.";
    }
});
