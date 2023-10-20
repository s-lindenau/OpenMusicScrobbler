async function scrobble(id) {
    scrobbleButtonElementId = "scrobbleButton";
    scrobbleResultElementId = "scrobbleResultFeedback";

    document.getElementById(scrobbleButtonElementId).disabled = true;
    document.getElementById(scrobbleResultElementId).innerHTML = "Submitting scrobble...";

    result = await postScrobble(id);
    document.getElementById(scrobbleResultElementId).innerHTML = result.message;

    document.getElementById(scrobbleButtonElementId).disabled = false;
}

async function postScrobble(id) {
    const url = "/app/collection/scrobble?id=" + id;
    const parameters = {method: "POST"};
    response = await fetch(url, parameters);
    data = await response.json();
    return data;
}