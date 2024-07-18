/**
 * Scrobble the selected release and update the document with feedback
 * @returns {Promise<void>} no data is returned when this Promise completes
 */
async function scrobble() {
    const scrobbleButtonElementId = "scrobbleButton";
    const scrobbleResultElementId = "scrobbleResultFeedback";
    const scrobbleFormElementId = "scrobbleRequestForm";

    document.getElementById(scrobbleButtonElementId).disabled = true;
    document.getElementById(scrobbleResultElementId).innerHTML = "Submitting scrobble...";

    let scrobbleRequestForm = document.getElementById(scrobbleFormElementId);
    let scrobbleResult = await postScrobble(scrobbleRequestForm);
    document.getElementById(scrobbleResultElementId).innerHTML = scrobbleResult.message;

    document.getElementById(scrobbleButtonElementId).disabled = false;
}

/**
 * Call the API endpoint to scrobble the given release by posting the provided form
 * @param form the form element capturing the details of the release to scrobble
 * @returns {Promise<any>} the result of the POST scrobble call as JSON data mapped from 'ScrobbleResult' object
 */
async function postScrobble(form) {
    const url = form.action;
    const urlEncodedFormData = new URLSearchParams(new FormData(form));
    const parameters = {method: "POST", body: urlEncodedFormData};
    let response = await fetch(url, parameters);
    return await response.json();
}