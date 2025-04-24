/**
 * Import modules.
 * WebJars are used on the server side in DropWizard to load common js libraries
 */
import moment from "./webjars/momentjs/moment.js";

/**
 * Constants for identifiers of elements in our DOM
 */
const scrobbleButtonElementId = "scrobbleButton";
const scrobbleResultElementId = "scrobbleResultFeedback";
const scrobbleFormElementId = "scrobbleRequestForm";
const scrobbleDateElementId = "scrobbleDate";
const scrobbleDateElementName = "lastTrackEndedAt";

/**
 * Execute on script load
 */
document.addEventListener("DOMContentLoaded", setScrobbleDateInputValueAndMax);
document.getElementById(scrobbleButtonElementId).addEventListener("click", scrobble);

/**
 * Scrobble the selected release and update the document with feedback
 * @returns {Promise<void>} no data is returned when this Promise completes
 */
async function scrobble() {
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
    const formData = asFormDataWithScrobbleDateInISOFormat(form);
    const urlEncodedFormData = new URLSearchParams(formData);
    const parameters = {method: "POST", body: urlEncodedFormData};
    return await fetchResponseAsJson(url, parameters);
}

/**
 * Wrap the Form element as FormData, and transform the ScrobbleDate value from local time to ISO compatible String
 * @param form the form element capturing the details of the release to scrobble
 * @returns {FormData} the form input fields as FormData, with the scrobbleDate value transformed to an ISO compatible String
 */
function asFormDataWithScrobbleDateInISOFormat(form) {
    const formData = new FormData(form);
    const scrobbleDateLocalString = formData.get(scrobbleDateElementName);

    if (scrobbleDateLocalString !== null && scrobbleDateLocalString.length > 0) {
        const scrobbleDate = new Date(scrobbleDateLocalString);
        const scrobbleDateISOString = moment(scrobbleDate).format("YYYY-MM-DDTHH:mm:ssZ");
        formData.set(scrobbleDateElementName, scrobbleDateISOString);
    }

    return formData;
}

/**
 * Set the current time as the form input limit for ScrobbleDate
 * @returns {void}
 */
function setScrobbleDateInputValueAndMax() {
    const currentDateTime = new Date();
    const currentDateTimeFormInputFormatted = moment(currentDateTime).format("YYYY-MM-DDTHH:mm")

    document.getElementById(scrobbleDateElementId).setAttribute("value", currentDateTimeFormInputFormatted);
    document.getElementById(scrobbleDateElementId).setAttribute("max", currentDateTimeFormInputFormatted);
}

/**
 * Fetch the provided endpoint and return the result as JSON
 * @param url the endpoint URL
 * @param parameters the parameters for the fetch operation
 * @returns {Promise<any>}
 */
async function fetchResponseAsJson(url, parameters) {
    try {
        let response = await fetch(url, parameters);
        return await response.json();
    } catch (error) {
        return getErrorMessage(error);
    }
}

/**
 * Process any errors and return as error message JSON in 'WebApplicationResponse' format
 * @param error the caught error
 * @returns {{success: boolean, message: string}} the error object
 */
function getErrorMessage(error) {
    return {
        success: false,
        message: "The application failed to process the request! Error message: " + error.message
    };
}