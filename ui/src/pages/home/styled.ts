import styled from 'styled-components';

export const UpperHalfContainer = styled.div`
  justify-content: space-between;
`;

export const WelcomeContainer = styled.div`
  border-bottom: 2px solid #808080;
`;

export const WelcomeMessage = styled.h1`
  font-family: Arial, sans-serif;
  font-style: normal;
`;

export const WelcomeMessageContainer = styled.div`
  display: inline-block;
`;

export const TopRightButtonsContainer = styled.div`
  display: inline-block;
  float: inline-end;
`;

export const CatchyMessageContainer = styled.div`
  display: inline-block;
  float: inline-start;
  verticalalign: middle;
`;

export const CatchyMessage = styled.h2`
  font-family: Arial, sans-serif;
  font-style: normal;
`;

export const CarLogoContainer = styled.div`
  height: 60;
  width: 100;
  maxHeight: { xs: 60, md: 60 };
  maxWidth: { xs: 100, md: 100 };
  marginRight: "10px";
`;
