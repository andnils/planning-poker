import * as style from "./Player.module.css";

export function Player({ name, vote }) {

  return (
      <div className={style.playercard}>
      <h6> { name } </h6>
      <div className={style.card}>
        { vote || '-' }
      </div>
    </div>
  );
}
