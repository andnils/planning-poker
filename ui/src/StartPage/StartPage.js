import { useState } from 'react';
import { useNavigate, useParams } from 'react-router';


export function StartPage() {
	const { roomId } = useParams();
	const [room, setRoom] = useState(roomId || "");
	const [name, setName] = useState("");
	const navigate = useNavigate();

	const go = () => {
		navigate(`/${room}/${name}`);
	}

	return (
		<div className="container-sm">
			<h1>Planning Poker</h1>
		<form>
			<div className="mb-3">
				<label htmlFor="displayName" className="form-label">Display name:</label>
				<input type="text" className="form-control" id="displayName" autoFocus={true} required={true} value={name} onChange={(e) => setName(e.target.value)}/>
			</div>
			<div className="mb-3">
				<label htmlFor="roomName" className="form-label">Room:</label>
				<input type="text" className="form-control" id="roomName" disabled={!!roomId} required={true} value={room} onChange={(e) => setRoom(e.target.value)}/>
			</div>

			<button type="submit" className="btn btn-primary" onClick={go}>Join room</button>
		</form>
		</div>
	);
}