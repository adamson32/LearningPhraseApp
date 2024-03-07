document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('formPhraseModal');
    const phraseInput = document.getElementById('phrase');
    const meaningInput = document.getElementById('meaning');
    const modal = document.getElementById('phraseModal');
    var scrollToBottom = localStorage.getItem('scrollToBottom');
    var existingPhrases = document.querySelectorAll('td:nth-child(1)')


    form.addEventListener('submit', function (event) {
        if (event.submitter && event.submitter.id === 'deleteBtn') {
            return;
        }
        if (phraseInput.value.trim() === '' || meaningInput.value.trim() === '') {
            event.preventDefault();
            modal.style.display = 'block';
        }
        if (phraseInput.value.trim() === '') {
            phraseInput.style.borderColor = 'red';
        }
        if (meaningInput.value.trim() === '') {
            console.log(phraseInput.value);
            meaningInput.style.borderColor = 'red';
        }
        for (var i = 0; i < existingPhrases.length; i++) {
            if (existingPhrases[i].textContent.trim() === phraseInput.value.trim() &&
                phraseInputTemp.trim() !== phraseInput.value.trim()) {
                event.preventDefault();
                alert("This phrase already exists!");
            }
        }
    });

    phraseInput.addEventListener('input', function () {
        if (phraseInput.value.trim() !== '') {
            phraseInput.style.borderColor = '';
        }
    });

    meaningInput.addEventListener('input', function () {
        if (meaningInput.value.trim() !== '') {
            meaningInput.style.borderColor = '';
        }
    });

});





