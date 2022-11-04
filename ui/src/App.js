import { Player } from "./Player/Player.js";
import style from "./App.module.css";

export function App() {
  return (
    <div className={style.board}>
      <Player name="Sven" vote="8"/>
      <Player name="Kalle"vote="12" />
      <Player name="Klas-Göran..."vote="123" />
    </div>
  );
}
