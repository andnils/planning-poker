import { useState, useEffect} from 'react';
import useWebSocket, { ReadyState } from 'react-use-websocket';
import { Player } from "./Player/Player.js";
import * as style from "./App.module.css";

export function App({ roomId, name }) {
  const socketUrl = `ws://${location.host}/api/ws/${roomId}/${name}`;

  const { sendMessage, lastMessage, readyState } = useWebSocket(socketUrl);

  const appState = lastMessage ? JSON.parse(lastMessage.data) : [];

  console.dir(lastMessage);

  const connectionStatus = {
    [ReadyState.CONNECTING]: 'Connecting',
    [ReadyState.OPEN]: 'Open',
    [ReadyState.CLOSING]: 'Closing',
    [ReadyState.CLOSED]: 'Closed',
    [ReadyState.UNINSTANTIATED]: 'Uninstantiated',
  }[readyState];


  return (
    <div className={style.board}>
      { appState.map(player => <Player key={player.name} name={player.name} vote={player.vote} />)}
    </div>
  );
}
