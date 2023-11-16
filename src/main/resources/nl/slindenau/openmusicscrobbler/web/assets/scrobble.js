/**
 * Scrobble the selected release and update the document with feedback
 * @param id the discogs ID of the release to scrobble
 * @returns {Promise<void>} no data is returned when this Promise completes
 */
async function scrobble(id) {
    const scrobbleButtonElementId = "scrobbleButton";
    const scrobbleResultElementId = "scrobbleResultFeedback";

    document.getElementById(scrobbleButtonElementId).disabled = true;
    document.getElementById(scrobbleResultElementId).innerHTML = "Submitting scrobble...";

    let result = await postScrobble(id);
    document.getElementById(scrobbleResultElementId).innerHTML = result.message;

    document.getElementById(scrobbleButtonElementId).disabled = false;
}

/**
 * Call the API method to scrobble the given release
 * @param id the discogs ID of the release to scrobble
 * @returns {Promise<any>} the result of the POST scrobble call as JSON data
 */
async function postScrobble(id) {
    const url = "/app/collection/scrobble?id=" + id;
    const parameters = {method: "POST"};
    let response = await fetch(url, parameters);
    return await response.json();
}