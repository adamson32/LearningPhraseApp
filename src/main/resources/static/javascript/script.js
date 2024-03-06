document.getElementById('cancelDelete').addEventListener('click', function () {
    document.getElementById('confirmationModal').style.display = 'none';
    document.getElementById('overlay').style.display = 'none';
});

document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('formAdd');
    var nameButtons = document.querySelectorAll('#groupNameButton');
    const nameInput = document.getElementById('inputName');
    const modal = document.getElementById('modal');
    const warningMessage = document.getElementById('warningMessage');
    var scrollToBottom = localStorage.getItem('scrollToBottom');

    form.addEventListener('submit', function (event) {
        const enteredName = nameInput.value;
        let nameExists = false;

        nameButtons.forEach(function (button) {
            if (button.innerText === enteredName) {
                nameExists = true;
            }
        });

        if (nameInput.value.trim() === '') {
            event.preventDefault();
            modal.style.display = 'block';
            warningMessage.style.display = 'block';
        } else if (nameExists) {
            event.preventDefault();
            alert('This name already exists. Please choose another name.');
        } else {
            warningMessage.style.display = 'none';
        }
    });

    nameInput.addEventListener('input', function () {
        if (nameInput.value.trim() !== '') {
            warningMessage.style.display = 'none';
        }
    });

    if (scrollToBottom === 'true') {
        window.scrollTo(0, document.body.scrollHeight);
        localStorage.removeItem('scrollToBottom'); // Usunięcie flagi po przewinięciu do dołu
    } else {
        var scrollpos = localStorage.getItem('scrollpos');
        if (scrollpos) window.scrollTo(0, scrollpos);
    }

    window.onbeforeunload = function (e) {
        localStorage.setItem('scrollpos', window.scrollY);
    };
});


document.getElementById('openModal').addEventListener('click', function () {
    document.getElementById('modal').style.display = 'block';
});


document.getElementById("addGroup").addEventListener("click", function () {
    localStorage.setItem('scrollToBottom', 'true');

});

document.getElementById('cancelModal').addEventListener('click', function () {
    document.getElementById('modal').style.display = 'none';
    document.getElementById('inputName').value = '';
    document.getElementById('inputDescription').value = '';
    document.getElementById('imagePreview').src = '';
    document.getElementById('formAdd').reset();
    warningMessage.style.display = 'none';
});

document.getElementById('addImage').addEventListener('click', function () {
    document.getElementById('imageUpload').click();
});

document.getElementById('imageUpload').addEventListener('change', function (e) {
    const file = e.target.files[0];
    const imagePreview = document.getElementById('imagePreview');
    imagePreview.src = URL.createObjectURL(file);
    imagePreview.classList.remove('hidden');
    document.getElementById('inputDescription').style.maxHeight = '280px';
});

function toggleImagePreview() {
    const imagePreview = document.getElementById('imagePreview');
    imagePreview.classList.toggle('hidden');
    const textarea = document.getElementById('inputDescription');
    if (imagePreview.classList.contains('hidden')) {
        textarea.style.maxHeight = '540px';
    } else {
        textarea.style.maxHeight = '280px';
    }
}