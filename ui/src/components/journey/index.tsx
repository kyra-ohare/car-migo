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

// const StyledCard = styled(Card)`
//   box-shadow: 05px 6px 5px rgba(0, 0, 0, 0.2); /* Customize the shadow */
//   padding: 5px; /* Add padding around the card */
//   transition: transform 0.2s ease-in-out; /* Add a smooth transition */
//   margin-bottom: 15px;
//   width: 50%;

//   &:hover {
//     transform: translateY(-5px); /* Apply a slight lift on hover */
//   }
// `;

// function JourneyCard(props: any) {
//   const data = props.data;
//   return (
//     <Box display="flex" justifyContent="center" alignItems="center">
//       <StyledCard raised={true}>
//         <CardContent>
//           <Typography variant="body1">
//             <b>When?</b> {data.createdDate}
//           </Typography>
//           <Typography variant="body1">
//             <b>What time?</b> {data.createdDate}
//           </Typography>
//           <Typography variant="body1">
//             <b>Availability:</b> {data.maxPassengers}
//           </Typography>
//         </CardContent>
//         <CardActions style={{ display: "flex", justifyContent: "flex-end" }}>
//           <CustomButton label="Book now" />
//         </CardActions>
//       </StyledCard>
//     </Box>
//   );
// }
