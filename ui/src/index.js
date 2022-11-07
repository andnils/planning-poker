import React from "react";
import ReactDOM from "react-dom/client";
import {
  createBrowserRouter,
  RouterProvider,
  Route,
} from "react-router-dom";
import { App } from './App.js';

const name = new URLSearchParams(document.location.search).get("name");

const router = createBrowserRouter([
  {
    path: "/",
    element: <App roomId="abc123" name={name} />,
  },
]);

ReactDOM.createRoot(document.getElementById("app")).render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>
);
