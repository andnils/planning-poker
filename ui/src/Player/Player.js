export function Player({ name, vote, me }) {

  const titleStyle = me ? "card card-bold" : "card";
  return (
    <div className={titleStyle}>
      <div className={"card-body"}>
        <div className={"card-title"}> { name} </div>
        <p className={"card-text"}>
          { vote || '-' }
        </p>
      </div>
    </div>
  );
}
