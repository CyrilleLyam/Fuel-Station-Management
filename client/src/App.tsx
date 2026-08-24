import { useState } from "react";
import { Button } from "@/components/ui/button";

function App() {
  const [count, setCount] = useState(0);

  return (
    <div className="flex min-h-svh flex-col items-center justify-center gap-4">
      <h1 className="text-2xl font-semibold">Get started</h1>
      <p className="text-muted-foreground">
        Edit <code>src/App.tsx</code> and save to test HMR
      </p>
      <Button onClick={() => setCount((count) => count + 1)}>
        Count is {count}
      </Button>
    </div>
  );
}

export default App;
