import React, { useState } from "react";
import { Box, Card, CardContent, Typography, Button } from "@mui/material";

// Sample card data (replace with actual data from the server)
const sampleCards = [
  { id: 1, title: "Card 1", content: "Lorem ipsum..." },
  { id: 2, title: "Card 2", content: "Dolor sit amet..." },
  // Add more card data
];

const MyCard = ({ title, content }) => {
  return (
    <Card>
      <CardContent>
        <Typography variant="h6">{title}</Typography>
        <Typography variant="body2">{content}</Typography>
      </CardContent>
    </Card>
  );
};

const Playground = () => {
  const [showCards, setShowCards] = useState(false);

  const handleSearch = () => {
    // Simulate fetching card data from the server
    // Replace with actual API call
    setShowCards(true);
  };

  return (
    <div>
      <Button variant="contained" color="primary" onClick={handleSearch}>
        Search
      </Button>

      {showCards && (
        <Box>
          <Typography variant="h4" gutterBottom>
            Search Results
          </Typography>
          {sampleCards.map((card) => (
            <MyCard key={card.id} title={card.title} content={card.content} />
          ))}
        </Box>
      )}
    </div>
  );
};

export default Playground;
