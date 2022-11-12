import useWebSocket from 'react-use-websocket';
import { Player } from './Player/Player.js';
import * as style from './App.module.css';
import { useParams } from 'react-router';

const voteOpts = [1, 2, 3, 5, 8, 13, 21];


function ResetButton({ onClick }) {
  return (
    <button type="button" className={style.resetBtn + " btn btn-secondary"} onClick={onClick}>Reset</button>
  );
}
function VoteButton({score, onClick}) {
  return (
    <span className={style.btnbtn}>
      <button
        type="button"
        onClick={onClick}
        className="btn btn-primary">
        { score }
      </button>
    </span>
  );
}

export function App() {
  const { roomId, name } = useParams();

  const socketUrl = `ws://${location.host}/api/ws/${roomId}/${name}`;

  const { sendJsonMessage, lastMessage, readyState } = useWebSocket(socketUrl);

  const appState = lastMessage ? JSON.parse(lastMessage.data) : [];

  const vote = (roomId, name, score) => {
    console.log('.-..');
    const msg = {
      type: "vote",
      name: name,
      room: roomId,
      vote: score
    }
    sendJsonMessage(msg);
  };

  const reset = () => {
    const msg = {
      type: "reset",
      room: roomId
    }
    sendJsonMessage(msg);
  }



  return (
    <div className="container text-center">
      <div className={style.board}>
        { appState.map(player => <Player key={player.name} name={player.name} vote={player.vote} me={player.name === name}/>)}
      </div>
      <div>
        <div className="btn-group" role="group" aria-label="Basic example">
          { voteOpts.map(opt => <VoteButton key={opt} onClick={() => vote(roomId, name, opt)} score={opt}/>)}
        </div>
      </div>
      <ResetButton onClick={reset} />
    </div>
  );
}
