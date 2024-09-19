/**
 * Constants for identifiers of elements in our DOM
 */
const scrobbleButtonElementId = "scrobbleButton";
const scrobbleResultElementId = "scrobbleResultFeedback";
const scrobbleFormElementId = "scrobbleRequestForm";
const scrobbleDateElementId = "scrobbleDate";
const scrobbleDateElementName = "lastTrackEndedAt";

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
    let response = await fetch(url, parameters);
    return await response.json();
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
        const scrobbleDateISOString = scrobbleDate.toISOString();
        formData.set(scrobbleDateElementName, scrobbleDateISOString);
    }

    return formData;
}

/**
 * Set the current time as the form input limit for ScrobbleDate
 * @returns {void}
 */
function setScrobbleDateInputValueAndMax() {
    const locale = 'en-US';
    const currentDateTime = new Date();
    const year = new Intl.DateTimeFormat(locale, {year: 'numeric'}).format(currentDateTime);
    const month = new Intl.DateTimeFormat(locale, {month: '2-digit'}).format(currentDateTime);
    const day = new Intl.DateTimeFormat(locale, {day: '2-digit'}).format(currentDateTime);
    const hour = new Intl.DateTimeFormat(locale, {hour: '2-digit', hour12: false}).format(currentDateTime);
    // 2-digit doesn't work for minutes...
    const minute = asTwoDigits(currentDateTime.getMinutes());
    const currentDateTimeFormInputFormatted = `${year}-${month}-${day}T${hour}:${minute}`;

    document.getElementById(scrobbleDateElementId).setAttribute("value", currentDateTimeFormInputFormatted);
    document.getElementById(scrobbleDateElementId).setAttribute("max", currentDateTimeFormInputFormatted);
}

/**
 * Format the input number as two digits if needed (1 -> 01, 15 -> 15)
 * @param inputNumber any positive number [0, N]
 * @returns {string} the number as String with a leading zero added in case of < 10
 */
function asTwoDigits(inputNumber) {
    console.assert(inputNumber >= 0, "Negative numbers not supported! Input: " + inputNumber);
    return "" + (inputNumber < 10 ? "0" + inputNumber : inputNumber);
}