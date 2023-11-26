import { useNavigate } from "react-router-dom";
import { Box, Button, Grid } from "@mui/material";
import InfoIcon from "@mui/icons-material/Info";
import { useGetHeartbeat } from "../../hooks/useGetHeartbeat";
import { ActionAreaCard, Footer, Search } from "../../components/index";
import Car from "../../assets/car.png";
import {
  CatchyMessage,
  TopRightButtonsContainer,
  UpperHalfContainer,
  WelcomeContainer,
  WelcomeMessage,
  WelcomeMessageContainer,
} from "./styled";

const Homepage = () => {
  const { data } = useGetHeartbeat();
  console.log("expect the json obj from server");
  console.log(data);

  const navigate = useNavigate();
  const goToSignUp = () => {
    navigate("/sign-up");
  };

  const goToSignIn = () => {
    navigate("/sign-in");
  };

  return (
    <>
      <UpperHalfContainer>
        <WelcomeContainer>
          <WelcomeMessageContainer>
            <WelcomeMessage>Welcome to Car-Migo</WelcomeMessage>
          </WelcomeMessageContainer>
          <TopRightButtonsContainer>
            <Button variant="contained" onClick={goToSignUp}>
              Sign up
            </Button>
            <Button variant="contained" onClick={goToSignIn}>
              Sign in
            </Button>
          </TopRightButtonsContainer>
        </WelcomeContainer>
        <Grid container>
          <Grid
            item
            xs={8}
            sx={{
              display: "flex",
              justifyContent: "center",
              alignItems: "center",
            }}
          >
            <CatchyMessage>
              The best way to get to places fast, at low cost and, most
              importantly, eco-friendly.
            </CatchyMessage>
          </Grid>
          <Grid item xs={4}>
            <Box
              component="img"
              sx={{
                display: "inline-block",
                float: "inline-end",
                height: 240,
                width: 400,
                maxHeight: { xs: 240, md: 240 },
                maxWidth: { xs: 400, md: 400 },
                margin: "10px",
                verticalAlign: "middle",
              }}
              alt="car-migo"
              src={Car}
            />
          </Grid>
        </Grid>
        <Search />
      </UpperHalfContainer>
      <ActionAreaCard
        id="boo"
        label="boo"
        icon={<InfoIcon style={{ width: 60, height: 60 }} />}
        imageAlt="Information"
        title="What is it?"
        text={
          <div>
            It is a match-making system for drivers and passengers. You can
            either advertise rides or query them.
            <br />
            Drivers can create rides (a journey and a timeframe) while
            passengers can book them.
            <br />
            When inside the car share, you can enjoy the ride and make new
            friends.
            <br />
            Come onboard and eperience this new lifestyle.
          </div>
        }
      />
      <ActionAreaCard
        id="boo"
        label="boo"
        icon={<InfoIcon style={{ width: 60, height: 60 }} />}
        imageAlt="Information"
        title="Why?"
        text={
          <div>
            It is great for the environment once there will be less CO
            <sub>2</sub> released into the atmosphere.
            <br />
            Moreoever, there will be less traffic in our cities thus emergency
            vehicles will repond to emergencies more rapidly, less noise
            pollution, less road accidents, and you can make new friends to top
            it off.
            <br />
            The application is not about profiting but about car sharing so the
            passengers can pay the driver a fair amount for fuel costs.
          </div>
        }
      />
      <Footer />
    </>
  );
};

export default Homepage;
