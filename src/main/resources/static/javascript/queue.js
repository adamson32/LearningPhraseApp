class Queue {
    constructor() {
        this.items = [];
    }

    enqueue(element) {
        this.items.push(element);
    }

    dequeue() {
        if (this.isEmpty()) {
            return "Underflow";
        }
        return this.items.shift();
    }

    front() {
        if (this.isEmpty()) {
            return "No elements in Queue";
        }
        return this.items[0];
    }

    isEmpty() {
        return this.items.length === 0;
    }

    printQueue() {
        let str = "";
        for (let i = 0; i < this.items.length; i++) {
            str += this.items[i] + " ";
        }
        return str;
    }

    rotate() {
        if (this.isEmpty() || this.items.length === 1) {
            return;
        }

        const firstElement = this.items.shift();
        this.items.push(firstElement);
    }
}


const hiddenList = document.getElementById('hiddenData');
const liElements = hiddenList.getElementsByTagName('li');

const phrasesQueue = new Queue();

for (let i = 0; i < liElements.length; i++) {
    const phraseId = liElements[i].getAttribute('data-id');
    const phrase = liElements[i].getAttribute('data-phrase');
    const description = liElements[i].getAttribute('data-description');
    const meaning = liElements[i].getAttribute('data-meaning');
    const progress = liElements[i].getAttribute('data-progress');

    const phraseConst = {
        phraseId: phraseId,
        phrase: phrase,
        description: description,
        meaning: meaning,
        progress: progress
    };

    phrasesQueue.enqueue(phraseConst);
}

var phraseLabel = document.getElementById('phraseLabel');
var checkButton = document.getElementById('check');
var bottomButton = document.getElementById('bottomBtn');
var descriptionLabel = document.getElementById('descriptionLabel');
var meaningLabel = document.getElementById('meaningLabel');
var againButton = document.getElementById('againBtn');
var endScreen = document.getElementById('endScreen');
var dayButton = document.getElementById('dayBtn');
var option2Button = document.getElementById('option2');
var option3Button = document.getElementById('option3');
var option4Button = document.getElementById('option4');
var finishButton = document.getElementById('finishBtn');

checkButton.addEventListener('click', function () {
    checkButton.style.display = 'none';
    bottomButton.style.display = 'flex';
    descriptionLabel.style.display = 'block';
    meaningLabel.style.display = 'block';
});

againButton.addEventListener('click', function () {
    checkButton.style.display = 'block';
    bottomButton.style.display = 'none';
    descriptionLabel.style.display = 'none';
    meaningLabel.style.display = 'none';

    phrasesQueue.rotate();
    phraseLabel.innerHTML = phrasesQueue.front().phrase;
    descriptionLabel.innerHTML = phrasesQueue.front().description;
    meaningLabel.innerHTML = phrasesQueue.front().meaning;
});

function updateQueueDisplay() {
    phrasesQueue.dequeue();

    if (phrasesQueue.isEmpty()) {
        endScreen.style.display = 'block';
        finishButton.style.display = 'none';
        checkButton.style.display = 'none';
        phraseLabel.style.display = 'none';
        bottomButton.style.display = 'none';
    } else {
        phraseLabel.innerHTML = phrasesQueue.front().phrase;
        descriptionLabel.innerHTML = phrasesQueue.front().description;
        meaningLabel.innerHTML = phrasesQueue.front().meaning;
        checkButton.style.display = 'block';
        bottomButton.style.display = 'none';
        if (phrasesQueue.front().progress == 1) {
            option2Button.innerHTML = phrasesQueue.front().progress + " day";
        } else {
            option2Button.innerHTML = phrasesQueue.front().progress + " days";
        }
        option3Button.innerHTML = phrasesQueue.front().progress * 2 + " days";
        option4Button.innerHTML = phrasesQueue.front().progress * 3 + " days";


        if (finishButton.style.display === 'none') {
            finishButton.style.display = 'block';
        }


    }
    descriptionLabel.style.display = 'none';
    meaningLabel.style.display = 'none';
}
