document.addEventListener("DOMContentLoaded", () => {
    const chatBox = document.getElementById('chat-box');
    const userInput = document.getElementById('user-input');
    const sendButton = document.getElementById('send-button');

    const sendMessage = async () => {
        const question = userInput.value.trim();
        if (question === "") return;

        // Display user's message
        appendMessage(question, 'user-message');
        userInput.value = '';

        // Show loading indicator
        const loadingElement = appendMessage('Thinking...', 'bot-message loading');

        try {
            // Fetch answer from the API
            const response = await fetch(`/ask?question=${encodeURIComponent(question)}`);

            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }

            const data = await response.json();

            // Remove loading indicator and display bot's answer
            loadingElement.remove();
            appendMessage(data.answer, 'bot-message');

        } catch (error) {
            console.error('Error fetching API:', error);
            loadingElement.remove();
            appendMessage('Sorry, something went wrong. Please check the console for errors.', 'bot-message');
        }
    };

    const appendMessage = (text, className) => {
        const messageDiv = document.createElement('div');
        messageDiv.className = `message ${className}`;

        const p = document.createElement('p');
        p.textContent = text;

        messageDiv.appendChild(p);
        chatBox.appendChild(messageDiv);
        chatBox.scrollTop = chatBox.scrollHeight; // Auto-scroll to the latest message
        return messageDiv;
    };

    sendButton.addEventListener('click', sendMessage);
    userInput.addEventListener('keypress', (e) => {
        if (e.key === 'Enter') {
            sendMessage();
        }
    });
});
