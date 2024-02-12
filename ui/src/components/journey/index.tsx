import { Typography, Box } from "@mui/material";
import { CustomButton, JourneyCard } from "..";
import ArrowForwardOutlined from "@mui/icons-material/ArrowForwardOutlined";

export default function Journey(props: any) {
  const handleCloseResults = () => {
    props.state(false);
  };

  return (
    <Box sx={{ backgroundColor: "#f0f0f0", padding: 5 }}>
      <Typography variant="h6" sx={{ mb: "15px", display: "inline-flex" }}>
        <b>
          {props.departure}
          <ArrowForwardOutlined />
          {props.destination}
        </b>
      </Typography>
      {props.results.map((data: any) => (
        <JourneyCard key={data.id} data={data} />
      ))}
      <Box display="flex" justifyContent="flex-end">
        <CustomButton label="Close results" onClick={handleCloseResults} />
      </Box>
    </Box>
  );
}
