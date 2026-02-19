import { Suspense, use, useState } from "react";
import "./App.css";

const fetchStory = async (prompt) => {
  const response = await fetch(
    `/api/story/generate-scenes?story=${encodeURIComponent(prompt)}`,
  );

  if (!response.ok) {
    throw new Error("Failed to fetch story");
  }

  return response.text();
};

// The New Child Component using 'use'
// This component automatically "suspends" (pauses) until the promise resolves
const StoryScript = ({ storyPromise }) => {
  // The 'use' hook reads the value from the promise
  const story = use(storyPromise);

  return (
    <div className="script-output">
      <h3>Generated Script:</h3>
      <pre>{story}</pre>
    </div>
  );
};

function App() {
  const [prompt, setPrompt] = useState("A brave bird escaping a fire");
  const [currentStoryPromise, setCurrentStoryPromise] = useState(null);

  const handleGenerate = () => {
    setCurrentStoryPromise(fetchStory(prompt));
  };

  return (
    <div className="studio-container">
      <h1>🎬 StoryVerse Studio</h1>
      <div className="input-section">
        <textarea
          value={prompt}
          onChange={(e) => setPrompt(e.target.value)}
          placeholder="Enter your story idea..."
          rows={3}
        />
        <br />
        <button onClick={handleGenerate}>Action! (Generate Scenes)</button>
      </div>
      {/* This is the magic. 
                When 'StoryScript' hits the 'use()' hook, it pauses.
                React sees the pause and shows the 'fallback' (Director is thinking...) 
              */}
      {currentStoryPromise && (
        <Suspense
          fallback={
            <div className="loading-text">🎥 Director is thinking...</div>
          }
        >
          <StoryScript storyPromise={currentStoryPromise} />
        </Suspense>
      )}
    </div>
  );
}

export default App;
